package com.memtrip.eosreach.app.blockproducer

import com.memtrip.eosreach.api.blockproducer.BlockProducerDetails
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ViewBlockProducerRenderAction : MxRenderAction {
    object Idle : ViewBlockProducerRenderAction()
    object OnProgress : ViewBlockProducerRenderAction()
    object OnError : ViewBlockProducerRenderAction()
    data class OnInvalidUrl(val url: String) : ViewBlockProducerRenderAction()
    data class NavigateToUrl(val url: String) : ViewBlockProducerRenderAction()
    data class Populate(val blockProducerDetails: BlockProducerDetails) : ViewBlockProducerRenderAction()
}

interface ViewBlockProducerViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
    fun showTitle(blockProducerName: String)
    fun populate(blockProducerDetails: BlockProducerDetails)
    fun invalidUrl(url: String)
    fun navigateToUrl(url: String)
}

class ViewBlockProducerViewRenderer @Inject internal constructor() : MxViewRenderer<ViewBlockProducerViewLayout, ViewBlockProducerViewState> {
    override fun layout(layout: ViewBlockProducerViewLayout, state: ViewBlockProducerViewState): Unit = when (state.view) {
        ViewBlockProducerViewState.View.Idle -> {
        }
        ViewBlockProducerViewState.View.OnProgress -> {
            layout.showProgress()
        }
        ViewBlockProducerViewState.View.OnError -> {
            layout.showError()
        }
        is ViewBlockProducerViewState.View.OnInvalidUrl -> {
            layout.invalidUrl(state.view.url)
        }
        is ViewBlockProducerViewState.View.NavigateToUrl -> {
            layout.navigateToUrl(state.view.url)
        }
        is ViewBlockProducerViewState.View.Populate -> {
            layout.populate(state.view.blockProducerDetails)
        }
    }
}