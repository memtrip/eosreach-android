package com.memtrip.eosreach.app.account.vote.cast.proxy

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class CastProxyVoteRenderAction : MxRenderAction {
    object Idle : CastProxyVoteRenderAction()
    object OnProgress : CastProxyVoteRenderAction()
    data class OnError(val message: String, val log: String) : CastProxyVoteRenderAction()
    object OnSuccess : CastProxyVoteRenderAction()
    data class ViewLog(val log: String) : CastProxyVoteRenderAction()
}

interface CastProxyVoteViewLayout : MxViewLayout {
    fun showProgress()
    fun showError(message: String, log: String)
    fun onSuccess()
    fun viewLog(log: String)
}

class CastProxyVoteViewRenderer @Inject internal constructor() : MxViewRenderer<CastProxyVoteViewLayout, CastProxyVoteViewState> {
    override fun layout(layout: CastProxyVoteViewLayout, state: CastProxyVoteViewState): Unit = when (state.view) {
        CastProxyVoteViewState.View.Idle -> {
        }
        CastProxyVoteViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is CastProxyVoteViewState.View.OnError -> {
            layout.showError(state.view.message, state.view.log)
        }
        CastProxyVoteViewState.View.OnSuccess -> {
            layout.onSuccess()
        }
        is CastProxyVoteViewState.View.ViewLog -> {
            layout.viewLog(state.view.log)
        }
    }
}