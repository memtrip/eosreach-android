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
    data class OnSuccess(val accountView: AccountView) : AccountRenderAction()
    object OnErrorFetchingAccount : AccountRenderAction()
    object OnErrorFetchingBalances : AccountRenderAction()
    object NavigateToAccountList : AccountRenderAction()
    object NavigateToImportKey : AccountRenderAction()
    object NavigateToCreateAccount : AccountRenderAction()
    object NavigateToSettings : AccountRenderAction()
}

interface AccountViewLayout : MxViewLayout {
    fun showProgress()
    fun populateTitle(accountName: String)
    fun populate(accountView: AccountView, page: AccountPagerFragment.Page)
    fun showPrice(price: String)
    fun showPriceUnavailable()
    fun showGetAccountError()
    fun showGetBalancesError()
    fun navigateToAccountList()
    fun navigateToImportKey()
    fun navigateToCreateAccount()
    fun navigateToSettings()
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
        AccountViewState.View.NavigateToAccountList -> {
            layout.navigateToAccountList()
        }
        AccountViewState.View.NavigateToImportKey -> {
            layout.navigateToImportKey()
        }
        AccountViewState.View.NavigateToCreateAccount -> {
            layout.navigateToCreateAccount()
        }
        AccountViewState.View.NavigateToSettings -> {
            layout.navigateToSettings()
        }
    }
}