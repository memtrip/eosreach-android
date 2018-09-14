package com.memtrip.eosreach.app.account.resources.manage

import com.memtrip.mxandroid.MxViewState

data class ManageRamViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnError : View()
    }
}