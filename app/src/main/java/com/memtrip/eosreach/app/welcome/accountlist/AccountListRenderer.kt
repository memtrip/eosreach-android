package com.memtrip.eosreach.app.welcome.accountlist

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class AccountListRenderAction : MxRenderAction {
    object OnProgress : AccountListRenderAction()
    object NavigateToSplash : AccountListRenderAction()
    object OnError : AccountListRenderAction()
    object OnSuccess : AccountListRenderAction()
}

interface AccountListViewLayout : MxViewLayout {
    fun showProgress()
    fun navigateToSplash()
    fun showAccounts()
    fun showError()
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
        AccountListViewState.View.OnSuccess -> {
            layout.showAccounts()
        }
        AccountListViewState.View.OnError -> {
            layout.showError()
        }
    }
}