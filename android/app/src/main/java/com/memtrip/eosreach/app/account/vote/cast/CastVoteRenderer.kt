package com.memtrip.eosreach.app.account.vote.cast

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class CastVoteRenderAction : MxRenderAction {
    object Idle : CastVoteRenderAction()
    object OnProgress : CastVoteRenderAction()
    data class OnError(val message: String, val log: String) : CastVoteRenderAction()
    data class OnSuccess(val blockProducerName: String) : CastVoteRenderAction()
    object NavigateToBlockProducerList : CastVoteRenderAction()
    data class ViewLog(val log: String) : CastVoteRenderAction()
}

interface CastVoteViewLayout : MxViewLayout {
    fun showProgress()
    fun showError(message: String, log: String)
    fun onSuccess()
    fun navigateToBlockProducerList()
    fun viewLog(log: String)
}

class CastVoteViewRenderer @Inject internal constructor() : MxViewRenderer<CastVoteViewLayout, CastVoteViewState> {
    override fun layout(layout: CastVoteViewLayout, state: CastVoteViewState): Unit = when (state.view) {
        CastVoteViewState.View.Idle -> {
        }
        CastVoteViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is CastVoteViewState.View.OnError -> {
            layout.showError(state.view.message, state.view.log)
        }
        is CastVoteViewState.View.OnSuccess -> {
            layout.onSuccess()
        }
        CastVoteViewState.View.NavigateToBlockProducerList -> {
            layout.navigateToBlockProducerList()
        }
        is CastVoteViewState.View.ViewLog -> {
            layout.viewLog(state.view.log)
        }
    }
}