package com.memtrip.eosreach.app.account.resources.manage.manageram

import com.memtrip.mxandroid.MxViewIntent

sealed class RamConfirmIntent : MxViewIntent {
    object Idle : RamConfirmIntent()
    object Init : RamConfirmIntent()
    data class Confirm(
        val account: String,
        val kb: String,
        val commitType: RamCommitType
    ) : RamConfirmIntent()
}