package com.memtrip.eosreach.app.account.vote

import com.memtrip.eosreach.api.account.EosAccountVote
import com.memtrip.mxandroid.MxViewState

data class VoteViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        data class PopulateProxyVote(val proxyAccountName: String) : View()
        data class PopulateProducerVotes(val eosAccountVote: EosAccountVote) : View()
        object NoVoteCast : View()
        object NavigateToCastVote : View()
        object OnVoteForUsProgress : View()
        object OnVoteForUsSuccess : View()
        data class OnVoteForUsError(val message: String, val log: String) : View()
    }
}