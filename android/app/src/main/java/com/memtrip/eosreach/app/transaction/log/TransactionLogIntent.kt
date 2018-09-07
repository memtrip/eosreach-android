package com.memtrip.eosreach.app.transaction.log

import com.memtrip.mxandroid.MxViewIntent

sealed class TransactionLogIntent : MxViewIntent {
    object Init : TransactionLogIntent()
    data class ShowLog(val log: String) : TransactionLogIntent()
}