package com.memtrip.eosreach.app.welcome.accountcreated

import com.memtrip.mxandroid.MxViewState

data class AccountCreatedViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnError : View()
    }
}