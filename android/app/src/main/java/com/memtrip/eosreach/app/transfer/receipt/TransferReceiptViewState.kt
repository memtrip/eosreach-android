package com.memtrip.eosreach.app.transfer.receipt

import com.memtrip.mxandroid.MxViewState

data class TransferReceiptViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnError : View()
    }
}