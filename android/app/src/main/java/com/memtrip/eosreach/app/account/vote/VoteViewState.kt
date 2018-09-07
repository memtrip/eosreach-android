package com.memtrip.eosreach.app.account.vote

import com.memtrip.mxandroid.MxViewState

data class VoteViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnError : View()
    }
}