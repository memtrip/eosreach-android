package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ManageBandwidthRenderAction : MxRenderAction {
    object DelegateBandwidthTabIdle : ManageBandwidthRenderAction()
    object UnDelegateBandwidthTabIdle : ManageBandwidthRenderAction()
    data class Init(val eosAccount: EosAccount) : ManageBandwidthRenderAction()
}

interface ManageBandwidthViewLayout : MxViewLayout {
    fun populate(eosAccount: EosAccount, page: ManageBandwidthFragmentPagerAdapter.Page)
}

class ManageBandwidthViewRenderer @Inject internal constructor() : MxViewRenderer<ManageBandwidthViewLayout, ManageBandwidthViewState> {
    override fun layout(layout: ManageBandwidthViewLayout, state: ManageBandwidthViewState): Unit = when (state.view) {
        ManageBandwidthViewState.View.Idle -> {
        }
        is ManageBandwidthViewState.View.Populate -> {
            layout.populate(state.view.eosAccount, state.page)
        }
    }
}