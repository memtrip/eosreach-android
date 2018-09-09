package com.memtrip.eosreach.app.account.vote

import android.app.Application
import com.memtrip.eosreach.api.account.EosAccountVote
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class VoteViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<VoteIntent, VoteRenderAction, VoteViewState>(
    VoteViewState(view = VoteViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: VoteIntent): Observable<VoteRenderAction> = when (intent) {
        VoteIntent.Idle -> Observable.just(VoteRenderAction.Idle)
        is VoteIntent.Init -> Observable.just(populate(intent.eosAccountVote))
        VoteIntent.VoteForUse -> TODO()
        VoteIntent.NavigateToCastVote -> Observable.just(VoteRenderAction.NavigateToCastVote)
    }

    override fun reducer(previousState: VoteViewState, renderAction: VoteRenderAction): VoteViewState = when (renderAction) {
        VoteRenderAction.Idle -> previousState.copy(
            view = VoteViewState.View.Idle)
        is VoteRenderAction.PopulateProxyVote -> previousState.copy(
            view = VoteViewState.View.PopulateProxyVote(renderAction.proxyAccountName))
        is VoteRenderAction.PopulateProducerVotes -> previousState.copy(
            view = VoteViewState.View.PopulateProducerVotes(renderAction.eosAccountVote))
        VoteRenderAction.NoVoteCast -> previousState.copy(
            view = VoteViewState.View.NoVoteCast)
        VoteRenderAction.NavigateToCastVote -> previousState.copy(
            view = VoteViewState.View.NavigateToCastVote)
        is VoteRenderAction.OnVoteForUsError -> previousState.copy(
            view = VoteViewState.View.OnVoteForUsError(renderAction.error))
        VoteRenderAction.OnVoteForUsSuccess -> previousState.copy(
            view = VoteViewState.View.OnVoteForUsSuccess)
    }

    override fun filterIntents(intents: Observable<VoteIntent>): Observable<VoteIntent> = Observable.merge(
        intents.ofType(VoteIntent.Init::class.java).take(1),
        intents.filter {
            !VoteIntent.Init::class.java.isInstance(it)
        }
    )

    private fun populate(eosAccountVote: EosAccountVote?): VoteRenderAction {
        if (eosAccountVote != null) {
            if (eosAccountVote.hasDelegatedProxyVoter) {
                return VoteRenderAction.PopulateProxyVote(eosAccountVote.proxyVoterAccountName)
            } else if (eosAccountVote.producers.isNotEmpty()) {
                return VoteRenderAction.PopulateProducerVotes(eosAccountVote)
            } else {
                return VoteRenderAction.NoVoteCast
            }
        } else {
            return VoteRenderAction.NoVoteCast
        }
    }
}