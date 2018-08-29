package com.memtrip.eosreach.app.account

import com.memtrip.mxandroid.MxViewState

data class AccountViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnError : View()
    }
}