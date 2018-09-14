package com.memtrip.eosreach.app.account.resources.manage

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ManageBandwidthRenderAction : MxRenderAction {
    object OnProgress : ManageBandwidthRenderAction()
    object OnError : ManageBandwidthRenderAction()
}

interface ManageBandwidthViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
}

class ManageBandwidthViewRenderer @Inject internal constructor() : MxViewRenderer<ManageBandwidthViewLayout, ManageBandwidthViewState> {
    override fun layout(layout: ManageBandwidthViewLayout, state: ManageBandwidthViewState): Unit = when (state.view) {
        ManageBandwidthViewState.View.Idle -> {

        }
        ManageBandwidthViewState.View.OnProgress -> {
            layout.showProgress()
        }
        ManageBandwidthViewState.View.OnError -> {
            layout.showError()
        }
    }
}