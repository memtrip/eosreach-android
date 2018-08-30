package com.memtrip.eosreach.app.account.resources

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ResourcesRenderAction : MxRenderAction {
    object OnProgress : ResourcesRenderAction()
    object OnError : ResourcesRenderAction()
}

interface ResourcesViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
}

class ResourcesViewRenderer @Inject internal constructor() : MxViewRenderer<ResourcesViewLayout, ResourcesViewState> {
    override fun layout(layout: ResourcesViewLayout, state: ResourcesViewState): Unit = when (state.view) {
        ResourcesViewState.View.Idle -> {
        }
        ResourcesViewState.View.OnProgress -> {
            layout.showProgress()
        }
        ResourcesViewState.View.OnError -> {
            layout.showError()
        }
    }
}