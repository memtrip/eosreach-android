package com.memtrip.eosreach.app.account.actions

import com.memtrip.eosreach.api.actions.model.AccountAction
import com.memtrip.mxandroid.MxViewIntent

sealed class ViewTransferActionIntent : MxViewIntent {
    object Idle : ViewTransferActionIntent()
    data class Init(val accountAction: AccountAction.Transfer) : ViewTransferActionIntent()
    data class ViewTransactionBlockExplorer(val transactionId: String) : ViewTransferActionIntent()
}