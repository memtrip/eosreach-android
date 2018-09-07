package com.memtrip.eosreach.app.transaction.receipt

import com.memtrip.mxandroid.MxViewIntent

sealed class TransactionReceiptIntent : MxViewIntent {
    object Init : TransactionReceiptIntent()
}