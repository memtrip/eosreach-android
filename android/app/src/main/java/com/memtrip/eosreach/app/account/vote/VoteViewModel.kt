package com.memtrip.eosreach.app.account.vote

import android.app.Application
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.account.EosAccountVote
import com.memtrip.eosreach.api.vote.VoteRequest
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import java.util.Arrays.asList
import javax.inject.Inject

class VoteViewModel @Inject internal constructor(
    private val voteRequest: VoteRequest,
    application: Application
) : MxViewModel<VoteIntent, VoteRenderAction, VoteViewState>(
    VoteViewState(view = VoteViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: VoteIntent): Observable<VoteRenderAction> = when (intent) {
        VoteIntent.Idle -> Observable.just(VoteRenderAction.Idle)
        is VoteIntent.Init -> Observable.just(populate(intent.eosAccountVote))
        is VoteIntent.VoteForUs -> voteForUs(intent.eosAccount)
        VoteIntent.NavigateToCastProducerVote -> Observable.just(VoteRenderAction.NavigateToCastProducerVote)
        VoteIntent.NavigateToCastProxyVote -> Observable.just(VoteRenderAction.NavigateToCastProxyVote)
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
        VoteRenderAction.NavigateToCastProducerVote -> previousState.copy(
            view = VoteViewState.View.NavigateToCastProducerVote)
        VoteRenderAction.NavigateToCastProxyVote -> previousState.copy(
            view = VoteViewState.View.NavigateToCastProxyVote)
        VoteRenderAction.OnVoteForUsProgress -> previousState.copy(
            view = VoteViewState.View.OnVoteForUsProgress)
        VoteRenderAction.OnVoteForUsSuccess -> previousState.copy(
            view = VoteViewState.View.OnVoteForUsSuccess)
        is VoteRenderAction.OnVoteForUsError -> previousState.copy(
            view = VoteViewState.View.OnVoteForUsError(renderAction.message, renderAction.log))
    }

    override fun filterIntents(intents: Observable<VoteIntent>): Observable<VoteIntent> = Observable.merge(
        intents.ofType(VoteIntent.Init::class.java).take(1),
        intents.filter {
            !VoteIntent.Init::class.java.isInstance(it)
        }
    )

    private fun populate(eosAccountVote: EosAccountVote?): VoteRenderAction {
        return if (eosAccountVote != null) {
            if (eosAccountVote.hasDelegatedProxyVoter) {
                VoteRenderAction.PopulateProxyVote(eosAccountVote.proxyVoterAccountName)
            } else if (eosAccountVote.producers.isNotEmpty()) {
                VoteRenderAction.PopulateProducerVotes(eosAccountVote)
            } else {
                VoteRenderAction.NoVoteCast
            }
        } else {
            VoteRenderAction.NoVoteCast
        }
    }

    private fun voteForUs(eosAccount: EosAccount): Observable<VoteRenderAction> {
        return voteRequest.voteForProducer(
            eosAccount.accountName,
            asList(context().getString(R.string.app_block_producer_name))
        ).map { result ->
            if (result.success) {
                VoteRenderAction.OnVoteForUsSuccess
            } else {
                VoteRenderAction.OnVoteForUsError(
                    context().getString(R.string.vote_no_vote_for_us_error),
                    result.apiError!!.body)
            }
        }.toObservable().startWith(VoteRenderAction.OnVoteForUsProgress)
    }
}