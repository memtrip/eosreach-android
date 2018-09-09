package com.memtrip.eosreach.app.transaction.receipt

import com.memtrip.eos.http.rpc.model.transaction.response.TransactionReceipt
import com.memtrip.mxandroid.MxViewState

data class TransactionReceiptViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        data class Populate(val transactionReceipt: TransactionReceipt) : View()
        object NavigateToActions : View()
    }
}