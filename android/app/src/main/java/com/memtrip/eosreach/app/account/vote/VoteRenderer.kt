package com.memtrip.eosreach.app.account.vote

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class VoteRenderAction : MxRenderAction {
    object OnProgress : VoteRenderAction()
    object OnError : VoteRenderAction()
}

interface VoteViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
}

class VoteViewRenderer @Inject internal constructor() : MxViewRenderer<VoteViewLayout, VoteViewState> {
    override fun layout(layout: VoteViewLayout, state: VoteViewState): Unit = when (state.view) {
        VoteViewState.View.Idle -> {

        }
        VoteViewState.View.OnProgress -> {
            layout.showProgress()
        }
        VoteViewState.View.OnError -> {
            layout.showError()
        }
    }
}