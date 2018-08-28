package com.memtrip.eosreach.app.welcome.keyimported

import com.memtrip.mxandroid.MxViewState

data class KeyImportedViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object Done : View()
    }
}