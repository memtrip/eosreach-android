package com.memtrip.eosreach.app.account.resources.manage.manageram

import com.memtrip.mxandroid.MxViewIntent

sealed class RamFormIntent : MxViewIntent {
    object Init : RamFormIntent()
    object Idle : RamFormIntent()
    data class Commit(
        val amount: String,
        val ramCommitType: RamCommitType
    ) : RamFormIntent()
}