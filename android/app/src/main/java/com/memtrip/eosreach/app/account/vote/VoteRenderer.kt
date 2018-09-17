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
    object NavigateToCastProducerVote : VoteRenderAction()
    object NavigateToCastProxyVote : VoteRenderAction()
    object OnVoteForUsProgress : VoteRenderAction()
    data class OnVoteForUsError(val message: String, val log: String) : VoteRenderAction()
    object OnVoteForUsSuccess : VoteRenderAction()
}

interface VoteViewLayout : MxViewLayout {
    fun populateProxyVote(proxyVoter: String)
    fun populateProducerVotes(eosAccountVote: EosAccountVote)
    fun showNoVoteCast()
    fun navigateToCastProducerVote()
    fun navigateToCastProxyVote()
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
        VoteViewState.View.NavigateToCastProducerVote -> {
            layout.navigateToCastProducerVote()
        }
        VoteViewState.View.NavigateToCastProxyVote -> {
            layout.navigateToCastProxyVote()
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