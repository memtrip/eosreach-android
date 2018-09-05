package com.memtrip.eosreach.app.issue.importkey

import com.memtrip.mxandroid.MxViewState

data class ImportKeyViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnSuccess : View()
        data class OnError(val error: String) : View()
        object NavigateToGithubSource : View()
        object NavigateToSettings : View()
    }
}