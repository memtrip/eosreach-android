package com.memtrip.eosreach.app.account

import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class AccountRenderAction : MxRenderAction {
    object OnProgress : AccountRenderAction()
    data class OnSuccess(val eosAccount: EosAccount) : AccountRenderAction()
    object OnError : AccountRenderAction()
}

interface AccountViewLayout : MxViewLayout {
    fun showProgress()
    fun populate(eosAccount: EosAccount)
    fun showError()
}

class AccountViewRenderer @Inject internal constructor() : MxViewRenderer<AccountViewLayout, AccountViewState> {
    override fun layout(layout: AccountViewLayout, state: AccountViewState): Unit = when (state.view) {
        AccountViewState.View.Idle -> {
        }
        AccountViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is AccountViewState.View.OnSuccess -> {
            layout.populate(state.view.eosAccount)
        }
        AccountViewState.View.OnError -> {
            layout.showError()
        }
    }
}