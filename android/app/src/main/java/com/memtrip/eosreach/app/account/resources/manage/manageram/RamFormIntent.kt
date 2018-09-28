package com.memtrip.eosreach.app.account.resources.manage.manageram

import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.mxandroid.MxViewIntent

sealed class RamFormIntent : MxViewIntent {
    object Init : RamFormIntent()
    object Idle : RamFormIntent()
    data class ConvertKiloBytesToEOSCost(val kb: String, val costPerKb: Balance) : RamFormIntent()
    data class Commit(
        val kilobytes: String,
        val ramCommitType: RamCommitType
    ) : RamFormIntent()
}