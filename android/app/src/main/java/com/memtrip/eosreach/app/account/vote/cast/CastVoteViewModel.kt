package com.memtrip.eosreach.app.account.vote.cast

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class CastVoteViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<CastVoteIntent, CastVoteRenderAction, CastVoteViewState>(
    CastVoteViewState(view = CastVoteViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: CastVoteIntent): Observable<CastVoteRenderAction> = when (intent) {
        CastVoteIntent.CastProducerVoteTabIdle -> Observable.just(CastVoteRenderAction.CastProducerVoteTabIdle)
        CastVoteIntent.CastProxyVoteTabIdle -> Observable.just(CastVoteRenderAction.CastProxyVoteTabIdle)
        is CastVoteIntent.Init -> Observable.just(CastVoteRenderAction.Populate(intent.eosAccount))
    }

    override fun reducer(previousState: CastVoteViewState, renderAction: CastVoteRenderAction): CastVoteViewState = when (renderAction) {
        CastVoteRenderAction.CastProducerVoteTabIdle -> previousState.copy(
            view = CastVoteViewState.View.Idle,
            page = CastVotePagerFragment.Page.PRODUCER)
        CastVoteRenderAction.CastProxyVoteTabIdle -> previousState.copy(
            view = CastVoteViewState.View.Idle,
            page = CastVotePagerFragment.Page.PROXY)
        is CastVoteRenderAction.Populate -> previousState.copy(
            view = CastVoteViewState.View.Populate(renderAction.eosAccount))
    }

    override fun filterIntents(intents: Observable<CastVoteIntent>): Observable<CastVoteIntent> = Observable.merge(
        intents.ofType(CastVoteIntent.Init::class.java).take(1),
        intents.filter {
            !CastVoteIntent.Init::class.java.isInstance(it)
        }
    )
}