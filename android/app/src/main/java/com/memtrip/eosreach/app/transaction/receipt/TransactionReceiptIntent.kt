package com.memtrip.eosreach.app.transaction.receipt

import com.memtrip.eos.http.rpc.model.transaction.response.TransactionReceipt
import com.memtrip.mxandroid.MxViewIntent

sealed class TransactionReceiptIntent : MxViewIntent {
    data class Init(val transactionReceipt: TransactionReceipt) : TransactionReceiptIntent()
    object NavigateToActions : TransactionReceiptIntent()
}