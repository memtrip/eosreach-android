package com.memtrip.eosreach.app.account.vote.cast.producers

import android.app.Application
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.vote.VoteRequest
import com.memtrip.eosreach.app.account.vote.cast.CastVoteIntent
import com.memtrip.eosreach.app.account.vote.cast.CastVoteRenderAction
import com.memtrip.eosreach.app.account.vote.cast.CastVoteViewState
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single
import java.util.Arrays
import javax.inject.Inject

class CastProducersVoteViewModel @Inject internal constructor(
    private val voteRequest: VoteRequest,
    application: Application
) : MxViewModel<CastProducersVoteIntent, CastProducersVoteRenderAction, CastProducersVoteViewState>(
    CastProducersVoteViewState(view = CastProducersVoteViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: CastProducersVoteIntent): Observable<CastProducersVoteRenderAction> = when (intent) {
        CastProducersVoteIntent.Idle -> Observable.just(CastProducersVoteRenderAction.Idle)
        is CastProducersVoteIntent.Init -> Observable.just(CastProducersVoteRenderAction.OnProgress)
        is CastProducersVoteIntent.Vote -> vote(intent.voterAccountName, intent.blockProducerName)
        CastProducersVoteIntent.NavigateToBlockProducerList -> Observable.just(CastProducersVoteRenderAction.NavigateToBlockProducerList)
        is CastProducersVoteIntent.ViewLog -> Observable.just(CastProducersVoteRenderAction.ViewLog(intent.log))
    }

    override fun reducer(previousState: CastProducersVoteViewState, renderAction: CastProducersVoteRenderAction): CastProducersVoteViewState = when (renderAction) {
        CastProducersVoteRenderAction.Idle -> previousState.copy(
            view = CastProducersVoteViewState.View.Idle)
        CastProducersVoteRenderAction.OnProgress -> previousState.copy(
            view = CastProducersVoteViewState.View.OnProgress)
        is CastProducersVoteRenderAction.OnError -> previousState.copy(
            view = CastProducersVoteViewState.View.OnError(renderAction.message, renderAction.log))
        is CastProducersVoteRenderAction.OnSuccess -> previousState.copy(
            view = CastProducersVoteViewState.View.OnSuccess(renderAction.blockProducerName))
        CastProducersVoteRenderAction.NavigateToBlockProducerList -> previousState.copy(
            view = CastProducersVoteViewState.View.NavigateToBlockProducerList)
        is CastProducersVoteRenderAction.ViewLog -> previousState.copy(
            view = CastProducersVoteViewState.View.ViewLog(renderAction.log))
    }

    override fun filterIntents(intents: Observable<CastProducersVoteIntent>): Observable<CastProducersVoteIntent> = Observable.merge(
        intents.ofType(CastProducersVoteIntent.Init::class.java).take(1),
        intents.filter {
            !CastProducersVoteIntent.Init::class.java.isInstance(it)
        }
    )

    private fun vote(
        voterAccountName: String,
        blockProducerName: String
    ): Observable<CastProducersVoteRenderAction> {
        return voteRequest.vote(
            voterAccountName,
            Arrays.asList(blockProducerName)
        ).flatMap { result ->
            if (result.success) {
                Single.just(CastProducersVoteRenderAction.OnSuccess(blockProducerName))
            } else {
                Single.just(CastProducersVoteRenderAction.OnError(
                    context().getString(R.string.cast_vote_error_message),
                    result.apiError!!.body))
            }
        }.toObservable().startWith(CastProducersVoteRenderAction.OnProgress)
    }
}