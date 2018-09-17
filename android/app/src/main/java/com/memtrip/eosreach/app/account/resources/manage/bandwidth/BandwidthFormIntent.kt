package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.mxandroid.MxViewIntent

sealed class BandwidthFormIntent : MxViewIntent {
    data class Init(val contractAccountBalance: ContractAccountBalance) : BandwidthFormIntent()
    object Idle : BandwidthFormIntent()
    data class Confirm(
        val bandwidthCommitType: BandwidthCommitType,
        val netAmount: String,
        val cpuAmount: String,
        val fromAccount: String,
        val contractAccountBalance: ContractAccountBalance
    ) : BandwidthFormIntent()
}