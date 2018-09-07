package com.memtrip.eosreach.app.transaction.receipt

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

class TransferReceiptViewRenderer @Inject internal constructor() : MxViewRenderer<TransferReceiptViewLayout, TransactionReceiptViewState> {
    override fun layout(layout: TransferReceiptViewLayout, state: TransactionReceiptViewState): Unit = when (state.view) {
        TransactionReceiptViewState.View.Idle -> {
        }
        TransactionReceiptViewState.View.OnProgress -> {
            layout.showProgress()
        }
        TransactionReceiptViewState.View.OnError -> {
            layout.showError()
        }
    }
}