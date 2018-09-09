package com.memtrip.eosreach.app.issue.createaccount

import com.memtrip.mxandroid.MxViewState

data class CreateAccountViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        data class OnSuccess(val privateKey: String) : View()
        data class OnError(val error: String) : View()
        object Done : View()
    }
}