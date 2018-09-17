package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class BandwidthManageRenderAction : MxRenderAction {
    object DelegateBandwidthTabIdle : BandwidthManageRenderAction()
    object UnDelegateBandwidthTabIdle : BandwidthManageRenderAction()
    object Init : BandwidthManageRenderAction()
}

interface BandwidthManageViewLayout : MxViewLayout {
    fun populate(page: BandwidthManageFragmentPagerAdapter.Page)
}

class ManageBandwidthViewRenderer @Inject internal constructor() : MxViewRenderer<BandwidthManageViewLayout, BandwidthManageViewState> {
    override fun layout(layout: BandwidthManageViewLayout, state: BandwidthManageViewState): Unit = when (state.view) {
        BandwidthManageViewState.View.Idle -> {
        }
        BandwidthManageViewState.View.Populate -> {
            layout.populate(state.page)
        }
    }
}