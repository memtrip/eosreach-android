package com.memtrip.eosreach.app.account.vote

import android.app.Application
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.vote.VoteRequest
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single
import java.util.Arrays.asList
import javax.inject.Inject

class CastVoteViewModel @Inject internal constructor(
    private val voteRequest: VoteRequest,
    application: Application
) : MxViewModel<CastVoteIntent, CastVoteRenderAction, CastVoteViewState>(
    CastVoteViewState(view = CastVoteViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: CastVoteIntent): Observable<CastVoteRenderAction> = when (intent) {
        CastVoteIntent.Idle -> Observable.just(CastVoteRenderAction.Idle)
        is CastVoteIntent.Init -> Observable.just(CastVoteRenderAction.OnProgress)
        is CastVoteIntent.Vote -> vote(intent.voterAccountName, intent.blockProducerName)
        CastVoteIntent.NavigateToBlockProducerList -> Observable.just(CastVoteRenderAction.NavigateToBlockProducerList)
        is CastVoteIntent.ViewLog -> Observable.just(CastVoteRenderAction.ViewLog(intent.log))
    }

    override fun reducer(previousState: CastVoteViewState, renderAction: CastVoteRenderAction): CastVoteViewState = when (renderAction) {
        CastVoteRenderAction.Idle -> previousState.copy(
            view = CastVoteViewState.View.Idle)
        CastVoteRenderAction.OnProgress -> previousState.copy(
            view = CastVoteViewState.View.OnProgress)
        is CastVoteRenderAction.OnError -> previousState.copy(
            view = CastVoteViewState.View.OnError(renderAction.message, renderAction.log))
        is CastVoteRenderAction.OnSuccess -> previousState.copy(
            view = CastVoteViewState.View.OnSuccess(renderAction.blockProducerName))
        CastVoteRenderAction.NavigateToBlockProducerList -> previousState.copy(
            view = CastVoteViewState.View.NavigateToBlockProducerList)
        is CastVoteRenderAction.ViewLog -> previousState.copy(
            view = CastVoteViewState.View.ViewLog(renderAction.log))
    }

    override fun filterIntents(intents: Observable<CastVoteIntent>): Observable<CastVoteIntent> = Observable.merge(
        intents.ofType(CastVoteIntent.Init::class.java).take(1),
        intents.filter {
            !CastVoteIntent.Init::class.java.isInstance(it)
        }
    )

    private fun vote(
        voterAccountName: String,
        blockProducerName: String
    ): Observable<CastVoteRenderAction> {
        return voteRequest.vote(
            voterAccountName,
            asList(blockProducerName)
        ).flatMap { result ->
            if (result.success) {
                Single.just(CastVoteRenderAction.OnSuccess(blockProducerName))
            } else {
                Single.just(CastVoteRenderAction.OnError(
                    context().getString(R.string.cast_vote_error_message),
                    result.apiError!!.body))
            }
        }.toObservable().startWith(CastVoteRenderAction.OnProgress)
    }
}