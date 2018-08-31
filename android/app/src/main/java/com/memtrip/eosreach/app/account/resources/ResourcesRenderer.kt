package com.memtrip.eosreach.app.account.resources

import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ResourcesRenderAction : MxRenderAction {
    data class Populate(val eosAccount: EosAccount) : ResourcesRenderAction()
}

interface ResourcesViewLayout : MxViewLayout {
    fun populate(eosAccount: EosAccount)
}

class ResourcesViewRenderer @Inject internal constructor() : MxViewRenderer<ResourcesViewLayout, ResourcesViewState> {
    override fun layout(layout: ResourcesViewLayout, state: ResourcesViewState): Unit = when (state.view) {
        ResourcesViewState.View.Idle -> {
        }
        is ResourcesViewState.View.Populate -> {
            layout.populate(state.view.eosAccount)
        }
    }
}