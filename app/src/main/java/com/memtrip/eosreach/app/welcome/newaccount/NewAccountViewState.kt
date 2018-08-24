package com.memtrip.eosreach.app.welcome.newaccount

import com.memtrip.mxandroid.MxViewState

data class NewAccountViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnError : View()
    }
}