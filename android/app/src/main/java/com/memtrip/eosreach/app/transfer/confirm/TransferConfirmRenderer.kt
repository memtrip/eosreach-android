package com.memtrip.eosreach.app.transfer.confirm

import com.memtrip.eosreach.api.transfer.TransferReceipt
import com.memtrip.eosreach.app.transfer.form.TransferFormData
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class TransferConfirmRenderAction : MxRenderAction {
    object Idle : TransferConfirmRenderAction()
    data class Populate(val transferFormData: TransferFormData) : TransferConfirmRenderAction()
    object OnProgress : TransferConfirmRenderAction()
    data class OnError(val message: String, val log: String) : TransferConfirmRenderAction()
    data class OnSuccess(val transferReceipt: TransferReceipt) : TransferConfirmRenderAction()
    data class ViewLog(val log: String) : TransferConfirmRenderAction()
}

interface TransferConfirmViewLayout : MxViewLayout {
    fun populate(transferFormData: TransferFormData)
    fun showProgress()
    fun onSuccess(transferReceipt: TransferReceipt)
    fun showError(message: String, log: String)
    fun viewLog(log: String)
}

class TransferConfirmViewRenderer @Inject internal constructor() : MxViewRenderer<TransferConfirmViewLayout, TransferConfirmViewState> {
    override fun layout(layout: TransferConfirmViewLayout, state: TransferConfirmViewState): Unit = when (state.view) {
        TransferConfirmViewState.View.Idle -> {
        }
        TransferConfirmViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is TransferConfirmViewState.View.Populate -> {
            layout.populate(state.view.transferFormData)
        }
        is TransferConfirmViewState.View.OnSuccess -> {
            layout.onSuccess(state.view.transferReceipt)
        }
        is TransferConfirmViewState.View.OnError -> {
            layout.showError(state.view.message, state.view.log)
        }
        is TransferConfirmViewState.View.ViewLog -> {
            layout.viewLog(state.view.log)
        }
    }
}