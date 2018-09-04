package com.memtrip.eosreach.app.transfer.receipt

import com.memtrip.mxandroid.MxViewIntent

sealed class TransferReceiptIntent : MxViewIntent {
    object Init : TransferReceiptIntent()
}