package com.memtrip.eosreach.app.welcome.splash

import com.memtrip.mxandroid.MxViewState

data class SplashViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object NavigateToCreateAccount : View()
        object NavigateToImportKeys : View()
    }
}