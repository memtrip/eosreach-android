package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.eosreach.api.bandwidth.DelegatedBandwidth
import com.memtrip.mxandroid.MxViewIntent

sealed class DelegateBandwidthListIntent : MxViewIntent {
    object Idle : DelegateBandwidthListIntent()
    data class Init(val accountName: String) : DelegateBandwidthListIntent()
    data class NavigateToUndelegateBandwidth(val delegatedBandwidth: DelegatedBandwidth) : DelegateBandwidthListIntent()
}