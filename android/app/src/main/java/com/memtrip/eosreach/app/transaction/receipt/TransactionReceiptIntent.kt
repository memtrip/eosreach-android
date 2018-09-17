package com.memtrip.eosreach.app.transaction.receipt

import com.memtrip.eosreach.api.transfer.ActionReceipt
import com.memtrip.mxandroid.MxViewIntent

sealed class TransactionReceiptIntent : MxViewIntent {
    object Idle : TransactionReceiptIntent()
    data class Init(val actionReceipt: ActionReceipt) : TransactionReceiptIntent()
    data class NavigateToBlockExplorer(val transactionId: String) : TransactionReceiptIntent()
    object NavigateToActions : TransactionReceiptIntent()
    object NavigateToAccount : TransactionReceiptIntent()
}