package com.memtrip.eosreach.app.welcome.entry

import com.memtrip.mxandroid.MxViewState

data class EntryViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnError : View()
        object NavigateToSplash : View()
        object NavigateToAccount : View()
        object NavigateToAccountList : View()
    }
}