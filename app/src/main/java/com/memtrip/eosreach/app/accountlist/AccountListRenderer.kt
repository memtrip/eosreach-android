package com.memtrip.eosreach.app.accountlist

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class AccountListRenderAction : MxRenderAction {
    object OnProgress : AccountListRenderAction()
    object OnError : AccountListRenderAction()
}

interface AccountListViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
}

class AccountListViewRenderer @Inject internal constructor() : MxViewRenderer<AccountListViewLayout, AccountListViewState> {
    override fun layout(layout: AccountListViewLayout, state: AccountListViewState): Unit = when (state.view) {
        AccountListViewState.View.Idle -> {

        }
        AccountListViewState.View.OnProgress -> {
            layout.showProgress()
        }
        AccountListViewState.View.OnError -> {
            layout.showError()
        }
    }
}