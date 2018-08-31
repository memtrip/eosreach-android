package com.memtrip.eosreach.app.issue.createaccount

import com.memtrip.mxandroid.MxViewState

data class CreateAccountViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnSuccess : View()
        object OnError : View()
    }
}