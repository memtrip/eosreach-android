package com.memtrip.eosreach.app.transaction.receipt

import com.memtrip.eosreach.api.transfer.ActionReceipt
import com.memtrip.mxandroid.MxViewIntent

sealed class TransactionReceiptIntent : MxViewIntent {
    data class Init(val actionReceipt: ActionReceipt) : TransactionReceiptIntent()
    object NavigateToActions : TransactionReceiptIntent()
    object NavigateToAccount : TransactionReceiptIntent()
}