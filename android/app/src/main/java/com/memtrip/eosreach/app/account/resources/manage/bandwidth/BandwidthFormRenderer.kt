package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class BandwidthFormRenderAction : MxRenderAction {
    object Idle : BandwidthFormRenderAction()
    data class NavigateToConfirm(val bandwidthBundle: BandwidthBundle) : BandwidthFormRenderAction()
}

interface BandwidthFormViewLayout : MxViewLayout {
    fun navigateToConfirm(bandwidthBundle: BandwidthBundle)
}

class BandwidthFormViewRenderer @Inject internal constructor() : MxViewRenderer<BandwidthFormViewLayout, BandwidthFormViewState> {
    override fun layout(layout: BandwidthFormViewLayout, state: BandwidthFormViewState): Unit = when (state.view) {
        BandwidthFormViewState.View.Idle -> {
        }
        is BandwidthFormViewState.View.NavigateToConfirm -> {
            layout.navigateToConfirm(state.view.bandwidthBundle)
        }
    }
}