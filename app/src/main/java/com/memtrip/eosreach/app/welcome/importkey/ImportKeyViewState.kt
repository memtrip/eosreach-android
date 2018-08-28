package com.memtrip.eosreach.app.welcome.importkey

import com.memtrip.mxandroid.MxViewState

data class ImportKeyViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnSuccess : View()
        object OnError : View()
    }
}