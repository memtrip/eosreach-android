package com.memtrip.eosreach.app.welcome.accountlist

import com.memtrip.mxandroid.MxViewState

data class AccountListViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object NavigateToSplash : View()
        object OnProgress : View()
        object OnSuccess : View()
        object OnError : View()
    }
}