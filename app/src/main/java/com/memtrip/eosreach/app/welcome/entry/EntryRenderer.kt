package com.memtrip.eosreach.app.welcome.entry

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class AccountListRenderAction : MxRenderAction {
    object OnProgress : AccountListRenderAction()
    object OnError : AccountListRenderAction()
    object NavigateToSplash : AccountListRenderAction()
    object NavigateToAccount : AccountListRenderAction()
}

interface AccountListViewLayout : MxViewLayout {
    fun showProgress()
    fun navigateToSplash()
    fun showError()
    fun navigateToAccount()
}

class AccountListViewRenderer @Inject internal constructor() : MxViewRenderer<AccountListViewLayout, EntryViewState> {
    override fun layout(layout: AccountListViewLayout, state: EntryViewState): Unit = when (state.view) {
        EntryViewState.View.Idle -> {
        }
        EntryViewState.View.NavigateToSplash -> {
            layout.navigateToSplash()
        }
        EntryViewState.View.OnProgress -> {
            layout.showProgress()
        }
        EntryViewState.View.OnError -> {
            layout.showError()
        }
        is EntryViewState.View.NavigateToAccount -> {
            layout.navigateToAccount()
        }
    }
}