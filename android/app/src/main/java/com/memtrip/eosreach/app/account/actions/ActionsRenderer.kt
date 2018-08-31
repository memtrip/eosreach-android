package com.memtrip.eosreach.app.account.actions

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ActionsRenderAction : MxRenderAction {
    object OnProgress : ActionsRenderAction()
    object OnError : ActionsRenderAction()
}

interface ActionsViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
}

class ActionsViewRenderer @Inject internal constructor() : MxViewRenderer<ActionsViewLayout, ActionsViewState> {
    override fun layout(layout: ActionsViewLayout, state: ActionsViewState): Unit = when (state.view) {
        ActionsViewState.View.Idle -> {
        }
        ActionsViewState.View.OnProgress -> {
            layout.showProgress()
        }
        ActionsViewState.View.OnError -> {
            layout.showError()
        }
    }
}