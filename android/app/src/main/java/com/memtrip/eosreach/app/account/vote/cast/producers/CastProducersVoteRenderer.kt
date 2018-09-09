package com.memtrip.eosreach.app.account.vote.cast.producers

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class CastProducersVoteRenderAction : MxRenderAction {
    object OnProgress : CastProducersVoteRenderAction()
    object OnError : CastProducersVoteRenderAction()
}

interface CastProducersVoteViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
}

class CastProducersVoteViewRenderer @Inject internal constructor() : MxViewRenderer<CastProducersVoteViewLayout, CastProducersVoteViewState> {
    override fun layout(layout: CastProducersVoteViewLayout, state: CastProducersVoteViewState): Unit = when (state.view) {
        CastProducersVoteViewState.View.Idle -> {

        }
        CastProducersVoteViewState.View.OnProgress -> {
            layout.showProgress()
        }
        CastProducersVoteViewState.View.OnError -> {
            layout.showError()
        }
    }
}