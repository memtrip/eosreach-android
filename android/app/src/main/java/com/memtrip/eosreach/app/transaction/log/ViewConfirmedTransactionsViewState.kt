package com.memtrip.eosreach.app.transaction.log

import com.memtrip.eosreach.db.transaction.TransactionLogEntity
import com.memtrip.mxandroid.MxViewState

data class ViewConfirmedTransactionsViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnError : View()
        data class Populate(val transactionLogEntities: List<TransactionLogEntity>) : View()
        data class NavigateToBlockExplorer(val transactionId: String) : View()
    }
}