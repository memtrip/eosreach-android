package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.mxandroid.MxViewState

data class BandwidthFormViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        data class NavigateToConfirm(val bandwidthBundle: BandwidthBundle) : View()
    }
}