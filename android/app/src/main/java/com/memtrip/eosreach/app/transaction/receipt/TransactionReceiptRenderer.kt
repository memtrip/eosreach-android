package com.memtrip.eosreach.app.transaction.receipt

import com.memtrip.eos.http.rpc.model.transaction.response.TransactionReceipt
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class TransferReceiptRenderAction : MxRenderAction {
    data class Populate(val transactionReceipt: TransactionReceipt) : TransferReceiptRenderAction()
    object NavigateToActions : TransferReceiptRenderAction()
}

interface TransferReceiptViewLayout : MxViewLayout {
    fun populate(transactionReceipt: TransactionReceipt)
    fun navigateToActions()
}

class TransferReceiptViewRenderer @Inject internal constructor() : MxViewRenderer<TransferReceiptViewLayout, TransactionReceiptViewState> {
    override fun layout(layout: TransferReceiptViewLayout, state: TransactionReceiptViewState): Unit = when (state.view) {
        TransactionReceiptViewState.View.Idle -> {
        }
        is TransactionReceiptViewState.View.Populate -> {
            layout.populate(state.view.transactionReceipt)
        }
        TransactionReceiptViewState.View.NavigateToActions -> {
            layout.navigateToActions()
        }
    }
}