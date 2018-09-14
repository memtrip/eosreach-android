package com.memtrip.eosreach.app.account.resources.manage.manageram

import com.memtrip.mxandroid.MxViewState

data class RamFormViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        data class OnError(val message: String, val log: String) : View()
        data class OnSuccess(val transactionId: String) : View()
    }
}