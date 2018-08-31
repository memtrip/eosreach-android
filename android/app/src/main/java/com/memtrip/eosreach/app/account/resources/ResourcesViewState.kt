package com.memtrip.eosreach.app.account.resources

import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.mxandroid.MxViewState

data class ResourcesViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        data class Populate(val eosAccount: EosAccount) : View()
    }
}