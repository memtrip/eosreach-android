package com.memtrip.eosreach.app.welcome.accountlist

import com.memtrip.eosreach.db.AccountEntity
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class AccountListRenderAction : MxRenderAction {
    object OnProgress : AccountListRenderAction()
    object NavigateToSplash : AccountListRenderAction()
    object OnError : AccountListRenderAction()
    data class OnSuccess(val accounts: List<AccountEntity>) : AccountListRenderAction()
    data class NavigateToAccount(val accountName: String) : AccountListRenderAction()
}

interface AccountListViewLayout : MxViewLayout {
    fun showProgress()
    fun navigateToSplash()
    fun showAccounts(accounts: List<AccountEntity>)
    fun showError()
    fun navigateToAccount(accountName: String)
}

class AccountListViewRenderer @Inject internal constructor() : MxViewRenderer<AccountListViewLayout, AccountListViewState> {
    override fun layout(layout: AccountListViewLayout, state: AccountListViewState): Unit = when (state.view) {
        AccountListViewState.View.Idle -> {
        }
        AccountListViewState.View.NavigateToSplash -> {
            layout.navigateToSplash()
        }
        AccountListViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is AccountListViewState.View.OnSuccess -> {
            layout.showAccounts(state.view.accounts)
        }
        AccountListViewState.View.OnError -> {
            layout.showError()
        }
        is AccountListViewState.View.NavigateToAccount -> {
            layout.navigateToAccount(state.view.accountName)
        }
    }
}