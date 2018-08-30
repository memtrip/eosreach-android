package com.memtrip.eosreach.app.account.resources

import com.memtrip.mxandroid.MxViewState

data class ResourcesViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnError : View()
    }
}