package com.memtrip.eosreach.app.account.resources.manage.manageram

import com.memtrip.eosreach.api.transfer.ActionReceipt
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class RamConfirmRenderAction : MxRenderAction {
    object Idle : RamConfirmRenderAction()
    object Populate : RamConfirmRenderAction()
    object OnProgress : RamConfirmRenderAction()
    data class OnSuccess(val transferReceipt: ActionReceipt) : RamConfirmRenderAction()
    data class OnError(val log: String) : RamConfirmRenderAction()
}

interface RamConfirmViewLayout : MxViewLayout {
    fun populate()
    fun showProgress()
    fun onSuccess(transferReceipt: ActionReceipt)
    fun showError(log: String)
}

class RamConfirmViewRenderer @Inject internal constructor() : MxViewRenderer<RamConfirmViewLayout, RamConfirmViewState> {
    override fun layout(layout: RamConfirmViewLayout, state: RamConfirmViewState): Unit = when (state.view) {
        RamConfirmViewState.View.Idle -> {
        }
        RamConfirmViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is RamConfirmViewState.View.OnError -> {
            layout.showError(state.view.log)
        }
        RamConfirmViewState.View.Populate -> {
            layout.populate()
        }
        is RamConfirmViewState.View.OnSuccess -> {
            layout.onSuccess(state.view.transferReceipt)
        }
    }
}