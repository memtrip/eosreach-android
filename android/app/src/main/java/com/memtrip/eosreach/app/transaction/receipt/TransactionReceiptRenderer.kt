package com.memtrip.eosreach.app.transaction.receipt

import com.memtrip.eosreach.api.transfer.ActionReceipt
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class TransferReceiptRenderAction : MxRenderAction {
    data class Populate(val actionReceipt: ActionReceipt) : TransferReceiptRenderAction()
    object NavigateToActions : TransferReceiptRenderAction()
    object NavigateToAccount : TransferReceiptRenderAction()
}

interface TransferReceiptViewLayout : MxViewLayout {
    fun populate(actionReceipt: ActionReceipt)
    fun navigateToActions()
    fun navigateToAccount()
}

class TransferReceiptViewRenderer @Inject internal constructor() : MxViewRenderer<TransferReceiptViewLayout, TransactionReceiptViewState> {
    override fun layout(layout: TransferReceiptViewLayout, state: TransactionReceiptViewState): Unit = when (state.view) {
        TransactionReceiptViewState.View.Idle -> {
        }
        is TransactionReceiptViewState.View.Populate -> {
            layout.populate(state.view.actionReceipt)
        }
        TransactionReceiptViewState.View.NavigateToActions -> {
            layout.navigateToActions()
        }
        TransactionReceiptViewState.View.NavigateToAccount -> {
            layout.navigateToAccount()
        }
    }
}