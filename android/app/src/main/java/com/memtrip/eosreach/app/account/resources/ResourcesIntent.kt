package com.memtrip.eosreach.app.account.resources

import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.mxandroid.MxViewIntent

sealed class ResourcesIntent : MxViewIntent {
    data class Init(val eosAccount: EosAccount) : ResourcesIntent()
    object Idle : ResourcesIntent()
    object NavigateToManageBandwidth : ResourcesIntent()
    object NavigateToManageRam : ResourcesIntent()
}