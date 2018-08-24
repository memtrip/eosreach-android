package com.memtrip.eosreach.app.welcome.accountcreated

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class AccountCreatedRenderAction : MxRenderAction {
    object OnProgress : AccountCreatedRenderAction()
    object OnError : AccountCreatedRenderAction()
}

interface AccountCreatedViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
}

class AccountCreatedViewRenderer @Inject internal constructor() : MxViewRenderer<AccountCreatedViewLayout, AccountCreatedViewState> {
    override fun layout(layout: AccountCreatedViewLayout, state: AccountCreatedViewState) = when (state.view) {
        AccountCreatedViewState.View.Idle -> {

        }
        AccountCreatedViewState.View.OnProgress -> {
            layout.showProgress()
        }
        AccountCreatedViewState.View.OnError -> {
            layout.showError()
        }
    }
}