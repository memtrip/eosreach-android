package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.mxandroid.MxViewIntent

sealed class BandwidthManageIntent : MxViewIntent {
    object DelegateBandwidthTabIdle : BandwidthManageIntent()
    object UnDelegateBandwidthTabIdle : BandwidthManageIntent()
    object Init : BandwidthManageIntent()
}