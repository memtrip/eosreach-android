package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.mxandroid.MxViewIntent

sealed class ManageBandwidthIntent : MxViewIntent {
    object DelegateBandwidthTabIdle : ManageBandwidthIntent()
    object UnDelegateBandwidthTabIdle : ManageBandwidthIntent()
    data class Init(
        val eosAccount: EosAccount
    ) : ManageBandwidthIntent()
}