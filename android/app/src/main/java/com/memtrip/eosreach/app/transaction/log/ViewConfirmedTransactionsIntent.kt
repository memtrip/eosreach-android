package com.memtrip.eosreach.app.transaction.log

import com.memtrip.mxandroid.MxViewIntent

sealed class ViewConfirmedTransactionsIntent : MxViewIntent {
    object Init : ViewConfirmedTransactionsIntent()
    object Idle : ViewConfirmedTransactionsIntent()
    data class NavigateToBlockExplorer(val transactionId: String) : ViewConfirmedTransactionsIntent()
}