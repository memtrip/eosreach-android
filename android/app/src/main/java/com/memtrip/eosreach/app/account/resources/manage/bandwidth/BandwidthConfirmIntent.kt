package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.mxandroid.MxViewIntent

sealed class BandwidthConfirmIntent : MxViewIntent {
    object Idle : BandwidthConfirmIntent()
    data class Init(val bandwidthBundle: BandwidthBundle) : BandwidthConfirmIntent()
    data class Commit(val bandwidthBundle: BandwidthBundle) : BandwidthConfirmIntent()
}