package com.memtrip.eosreach.app.account.actions

import com.memtrip.mxandroid.MxViewState

data class ActionsViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnError : View()
    }
}