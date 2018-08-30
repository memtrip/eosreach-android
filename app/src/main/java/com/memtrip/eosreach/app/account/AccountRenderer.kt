package com.memtrip.eosreach.app.account

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class AccountRenderAction : MxRenderAction {
    object OnProgress : AccountRenderAction()
    data class OnSuccess(val accountView: AccountView) : AccountRenderAction()
    data class OnErrorFetchingAccount(val accountName: String) : AccountRenderAction()
    data class OnErrorFetchingBalances(val accountName: String) : AccountRenderAction()
    object Idle : AccountRenderAction()
    object RefreshAccounts : AccountRenderAction()
    object NavigateToImportKey : AccountRenderAction()
    object NavigateToCreateAccount : AccountRenderAction()
    object NavigateToSettings : AccountRenderAction()
}

interface AccountViewLayout : MxViewLayout {
    fun showProgress()
    fun populate(accountView: AccountView)
    fun showGetAccountError(accountName: String)
    fun showGetBalancesError(accountName: String)
    fun navigateToImportKey()
    fun navigateToCreateAccount()
    fun navigateToSettings()
}

class AccountViewRenderer @Inject internal constructor() : MxViewRenderer<AccountViewLayout, AccountViewState> {
    override fun layout(layout: AccountViewLayout, state: AccountViewState): Unit = when (state.view) {
        AccountViewState.View.Idle -> {
        }
        AccountViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is AccountViewState.View.OnSuccess -> {
            layout.populate(state.view.accountView)
        }
        is AccountViewState.View.OnErrorFetchingAccount -> {
            layout.showGetAccountError(state.view.accountName)
        }
        is AccountViewState.View.OnErrorFetchingBalances -> {
            layout.showGetBalancesError(state.view.accountName)
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