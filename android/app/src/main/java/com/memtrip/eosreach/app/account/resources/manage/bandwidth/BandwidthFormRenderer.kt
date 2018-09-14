package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class BandwidthFormRenderAction : MxRenderAction {
    object Idle : BandwidthFormRenderAction()
    object OnProgress : BandwidthFormRenderAction()
    data class OnError(val message: String, val log: String) : BandwidthFormRenderAction()
    data class OnSuccess(val transactionId: String) : BandwidthFormRenderAction()
}

interface BandwidthFormViewLayout : MxViewLayout {
    fun showProgress()
    fun showError(message: String, log: String)
    fun showSuccess(transactionId: String)
}

class BandwidthFormViewRenderer @Inject internal constructor() : MxViewRenderer<BandwidthFormViewLayout, BandwidthFormViewState> {
    override fun layout(layout: BandwidthFormViewLayout, state: BandwidthFormViewState): Unit = when (state.view) {
        BandwidthFormViewState.View.Idle -> {
        }
        BandwidthFormViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is BandwidthFormViewState.View.OnError -> {
            layout.showError(state.view.message, state.view.log)
        }
        is BandwidthFormViewState.View.OnSuccess -> {
            layout.showSuccess(state.view.transactionId)
        }
    }
}