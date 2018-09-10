package com.memtrip.eosreach.app.account.vote

import com.memtrip.eosreach.api.account.EosAccountVote
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class VoteRenderAction : MxRenderAction {
    object Idle : VoteRenderAction()
    data class PopulateProxyVote(val proxyAccountName: String) : VoteRenderAction()
    data class PopulateProducerVotes(val eosAccountVote: EosAccountVote) : VoteRenderAction()
    object NoVoteCast : VoteRenderAction()
    object NavigateToCastVote : VoteRenderAction()
    object OnVoteForUsProgress : VoteRenderAction()
    data class OnVoteForUsError(val message: String, val log: String) : VoteRenderAction()
    object OnVoteForUsSuccess : VoteRenderAction()
}

interface VoteViewLayout : MxViewLayout {
    fun populateProxyVote(proxyVoter: String)
    fun populateProducerVotes(eosAccountVote: EosAccountVote)
    fun showNoVoteCast()
    fun navigateToCastVote()
    fun showVoteForUsProgress()
    fun voteForUsSuccess()
    fun voteForUsError(message: String, log: String)
}

class VoteViewRenderer @Inject internal constructor() : MxViewRenderer<VoteViewLayout, VoteViewState> {
    override fun layout(layout: VoteViewLayout, state: VoteViewState): Unit = when (state.view) {
        VoteViewState.View.Idle -> {
        }
        is VoteViewState.View.PopulateProxyVote -> {
            layout.populateProxyVote(state.view.proxyAccountName)
        }
        is VoteViewState.View.PopulateProducerVotes -> {
            layout.populateProducerVotes(state.view.eosAccountVote)
        }
        VoteViewState.View.NoVoteCast -> {
            layout.showNoVoteCast()
        }
        VoteViewState.View.NavigateToCastVote -> {
            layout.navigateToCastVote()
        }
        VoteViewState.View.OnVoteForUsProgress -> {
            layout.showVoteForUsProgress()
        }
        VoteViewState.View.OnVoteForUsSuccess -> {
            layout.voteForUsSuccess()
        }
        is VoteViewState.View.OnVoteForUsError -> {
            layout.voteForUsError(state.view.message, state.view.log)
        }
    }
}