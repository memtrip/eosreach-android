package com.memtrip.eosreach.app.account.vote.cast.proxy

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class CastProxyVoteRenderAction : MxRenderAction {
    object OnProgress : CastProxyVoteRenderAction()
    object OnError : CastProxyVoteRenderAction()
}

interface CastProxyVoteViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
}

class CastProxyVoteViewRenderer @Inject internal constructor() : MxViewRenderer<CastProxyVoteViewLayout, CastProxyVoteViewState> {
    override fun layout(layout: CastProxyVoteViewLayout, state: CastProxyVoteViewState): Unit = when (state.view) {
        CastProxyVoteViewState.View.Idle -> {

        }
        CastProxyVoteViewState.View.OnProgress -> {
            layout.showProgress()
        }
        CastProxyVoteViewState.View.OnError -> {
            layout.showError()
        }
    }
}