/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.memtrip.eosreach.app.account

import com.memtrip.eosreach.app.price.CurrencyPairFormatter
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class AccountRenderAction : MxRenderAction {
    object BalanceTabIdle : AccountRenderAction()
    object ResourceTabIdle : AccountRenderAction()
    object VoteTabIdle : AccountRenderAction()
    data class OnProgress(val accountName: String) : AccountRenderAction()
    data class OnProgressWithStartingTab(
        val accountName: String,
        val page: AccountFragmentPagerAdapter.Page
    ) : AccountRenderAction()
    data class OnSuccess(val accountView: AccountView) : AccountRenderAction()
    object OnErrorFetchingAccount : AccountRenderAction()
    object OnErrorFetchingBalances : AccountRenderAction()
}

interface AccountViewLayout : MxViewLayout {
    fun showProgress()
    fun populateTitle(accountName: String)
    fun populate(accountView: AccountView, page: AccountFragmentPagerAdapter.Page)
    fun showPrice(price: String)
    fun showPriceUnavailable()
    fun showGetAccountError()
    fun showGetBalancesError()
}

class AccountViewRenderer @Inject internal constructor() : MxViewRenderer<AccountViewLayout, AccountViewState> {

    override fun layout(layout: AccountViewLayout, state: AccountViewState) {

        state.accountName?.let {
            layout.populateTitle(it)
        }

        layoutState(layout, state)
    }

    private fun layoutState(layout: AccountViewLayout, state: AccountViewState): Unit = when (state.view) {
        AccountViewState.View.Idle -> {
        }
        is AccountViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is AccountViewState.View.OnSuccess -> {
            val accountView = state.accountView!!
            val balances = accountView.balances!!.balances
            val eosPrice = accountView.eosPrice!!

            layout.populate(accountView, state.page)

            if (balances.isNotEmpty()) {
                val eosBalance = balances[0].balance.amount
                if (eosPrice.unavailable) {
                    layout.showPriceUnavailable()
                } else {
                    val formattedPrice = CurrencyPairFormatter.formatAmountCurrencyPairValue(eosBalance, eosPrice)
                    if (accountView.eosPrice.unavailable) {
                        layout.showPriceUnavailable()
                    } else {
                        layout.showPrice(formattedPrice)
                    }
                }
            } else {
                layout.showPriceUnavailable()
            }
        }
        is AccountViewState.View.OnErrorFetchingAccount -> {
            layout.showGetAccountError()
        }
        is AccountViewState.View.OnErrorFetchingBalances -> {
            layout.showGetBalancesError()
        }
    }
}