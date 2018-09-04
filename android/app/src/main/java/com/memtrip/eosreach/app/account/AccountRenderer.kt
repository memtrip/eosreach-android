package com.memtrip.eosreach.app.account

import com.memtrip.eosreach.app.price.BalanceParser
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

sealed class AccountRenderAction : MxRenderAction {
    object Idle : AccountRenderAction()
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
    fun populate(accountView: AccountView)
    fun showPrice(price: String)
    fun showOutDatedPrice(price: String)
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

            layout.populate(accountView)

            if (balances.isNotEmpty()) {
                val eosBalance = balances[0].balance.amount
                if (eosPrice.unavailable) {
                    layout.showPriceUnavailable()
                } else {
                    val formattedPrice = BalanceParser.format(eosBalance * eosPrice.value, eosPrice.currency)
                    if (accountView.eosPrice.outOfDate) {
                        layout.showOutDatedPrice(formattedPrice)
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