package com.memtrip.eosreach.app.settings.eosendpoint

import com.memtrip.mxandroid.MxViewIntent

sealed class EosEndpointIntent : MxViewIntent {
    object Init : EosEndpointIntent()
    data class ChangeEndpoint(val endpoint: String) : EosEndpointIntent()
}