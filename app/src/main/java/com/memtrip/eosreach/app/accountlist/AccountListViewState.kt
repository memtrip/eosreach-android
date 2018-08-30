package com.memtrip.eosreach.app.accountlist

import com.memtrip.mxandroid.MxViewState

data class AccountListViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnError : View()
    }
}