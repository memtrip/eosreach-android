package com.memtrip.eosreach.app.account.resources.manage

import com.memtrip.mxandroid.MxViewIntent

sealed class ManageBandwidthIntent : MxViewIntent {
    object Init : ManageBandwidthIntent()
}