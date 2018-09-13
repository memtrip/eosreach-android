package com.memtrip.eosreach.app.transaction.log

import com.memtrip.mxandroid.MxViewIntent

sealed class ViewConfirmedTransactionsIntent : MxViewIntent {
    object Init : ViewConfirmedTransactionsIntent()
}