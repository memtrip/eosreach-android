package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.eosreach.api.bandwidth.DelegatedBandwidth
import com.memtrip.mxandroid.MxViewState

data class DelegateBandwidthListViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        object OnError : View()
        object Empty : View()
        data class Populate(val bandwidth: List<DelegatedBandwidth>) : View()
        data class NavigateToUndelegateBandwidth(val delegatedBandwidth: DelegatedBandwidth) : View()
    }
}