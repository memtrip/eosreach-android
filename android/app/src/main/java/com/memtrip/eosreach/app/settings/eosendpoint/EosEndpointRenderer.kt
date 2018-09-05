package com.memtrip.eosreach.app.settings.eosendpoint

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class EosEndpointRenderAction : MxRenderAction {
    object Idle : EosEndpointRenderAction()
    object OnProgress : EosEndpointRenderAction()
    data class OnError(val message: String) : EosEndpointRenderAction()
    object OnSuccess : EosEndpointRenderAction()
    object NavigateToBlockProducerList : EosEndpointRenderAction()
}

interface EosEndpointViewLayout : MxViewLayout {
    fun showProgress()
    fun showError(message: String)
    fun currentUrl(url: String)
    fun onSuccess()
    fun navigateToBlockProducerList()
}

class EosEndpointViewRenderer @Inject internal constructor() : MxViewRenderer<EosEndpointViewLayout, EosEndpointViewState> {
    override fun layout(layout: EosEndpointViewLayout, state: EosEndpointViewState) {
        layout.currentUrl(state.endpointUrl)
        layoutView(layout, state)
    }

    private fun layoutView(layout: EosEndpointViewLayout, state: EosEndpointViewState): Unit = when (state.view) {
        EosEndpointViewState.View.Idle -> {
        }
        EosEndpointViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is EosEndpointViewState.View.OnError -> {
            layout.showError(state.view.message)
        }
        EosEndpointViewState.View.OnSuccess -> {
            layout.onSuccess()
        }
        EosEndpointViewState.View.NavigateToBlockProducerList -> {
            layout.navigateToBlockProducerList()
        }
    }
}