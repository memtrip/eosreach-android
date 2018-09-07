package com.memtrip.eosreach.app.transaction.log

import com.memtrip.mxandroid.MxViewState

data class TransactionLogViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        data class ShowLog(val log: String) : View()
    }
}