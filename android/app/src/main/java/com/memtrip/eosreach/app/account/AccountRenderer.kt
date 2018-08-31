package com.memtrip.eosreach.app.account

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class AccountRenderAction : MxRenderAction {
    object Idle : AccountRenderAction()
    data class OnProgress(val accountName: String) : AccountRenderAction()
    data class OnSuccess(val accountView: AccountView) : AccountRenderAction()
    object OnErrorFetchingAccount : AccountRenderAction()
    object OnErrorFetchingBalances : AccountRenderAction()
    object NavigateToAccountList : AccountRenderAction()
    object RefreshAccounts : AccountRenderAction()
    object NavigateToImportKey : AccountRenderAction()
    object NavigateToCreateAccount : AccountRenderAction()
    object NavigateToSettings : AccountRenderAction()
}

interface AccountViewLayout : MxViewLayout {
    fun showProgress(accountName: String)
    fun populate(accountView: AccountView)
    fun showGetAccountError()
    fun showGetBalancesError()
    fun navigateToAccountList()
    fun navigateToImportKey()
    fun navigateToCreateAccount()
    fun navigateToSettings()
}

class AccountViewRenderer @Inject internal constructor() : MxViewRenderer<AccountViewLayout, AccountViewState> {
    override fun layout(layout: AccountViewLayout, state: AccountViewState): Unit = when (state.view) {
        AccountViewState.View.Idle -> {
        }
        is AccountViewState.View.OnProgress -> {
            layout.showProgress(state.view.accountName)
        }
        is AccountViewState.View.OnSuccess -> {
            layout.populate(state.view.accountView)
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