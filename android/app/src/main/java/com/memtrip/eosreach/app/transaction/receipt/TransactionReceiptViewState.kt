package com.memtrip.eosreach.app.transaction.receipt

import com.memtrip.mxandroid.MxViewState

data class TransactionReceiptViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnError : View()
    }
}