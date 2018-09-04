package com.memtrip.eosreach.app.transfer.receipt

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class TransferReceiptRenderAction : MxRenderAction {
    object OnProgress : TransferReceiptRenderAction()
    object OnError : TransferReceiptRenderAction()
}

interface TransferReceiptViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
}

class TransferReceiptViewRenderer @Inject internal constructor() : MxViewRenderer<TransferReceiptViewLayout, TransferReceiptViewState> {
    override fun layout(layout: TransferReceiptViewLayout, state: TransferReceiptViewState): Unit = when (state.view) {
        TransferReceiptViewState.View.Idle -> {
        }
        TransferReceiptViewState.View.OnProgress -> {
            layout.showProgress()
        }
        TransferReceiptViewState.View.OnError -> {
            layout.showError()
        }
    }
}