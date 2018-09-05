package com.memtrip.eosreach.app.settings.eosendpoint

import com.memtrip.mxandroid.MxViewState
import com.memtrip.mxandroid.MxViewState.Companion.id

data class EosEndpointViewState(
    val endpointUrl: String,
    val view: View
) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        data class OnError(val message: String, val unique: Int = id()) : View()
        object OnSuccess : View()
        object NavigateToBlockProducerList : View()
    }
}