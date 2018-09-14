package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.mxandroid.MxViewState

data class BandwidthConfirmViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        data class Populate(val bandwidthBundle: BandwidthBundle) : View()
        object OnProgress : View()
        data class OnError(val message: String, val log: String) : View()
        data class OnSuccess(val transactionId: String) : View()
    }
}