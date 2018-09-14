package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class BandwidthConfirmRenderAction : MxRenderAction {
    object Idle : BandwidthConfirmRenderAction()
    data class Populate(val bandwidthBundle: BandwidthBundle) : BandwidthConfirmRenderAction()
    object OnProgress : BandwidthConfirmRenderAction()
    data class OnError(val message: String, val log: String) : BandwidthConfirmRenderAction()
    data class OnSuccess(val transactionId: String) : BandwidthConfirmRenderAction()
}

interface BandwidthConfirmViewLayout : MxViewLayout {
    fun populate(bandwidthBundle: BandwidthBundle)
    fun showProgress()
    fun showError(message: String, log: String)
    fun showSuccess(transactionId: String)
}

class BandwidthConfirmViewRenderer @Inject internal constructor() : MxViewRenderer<BandwidthConfirmViewLayout, BandwidthConfirmViewState> {
    override fun layout(layout: BandwidthConfirmViewLayout, state: BandwidthConfirmViewState): Unit = when (state.view) {
        BandwidthConfirmViewState.View.Idle -> {
        }
        is BandwidthConfirmViewState.View.Populate -> {
            layout.populate(state.view.bandwidthBundle)
        }
        BandwidthConfirmViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is BandwidthConfirmViewState.View.OnError -> {
            layout.showError(state.view.message, state.view.log)
        }
        is BandwidthConfirmViewState.View.OnSuccess -> {
            layout.showSuccess(state.view.transactionId)
        }
    }
}