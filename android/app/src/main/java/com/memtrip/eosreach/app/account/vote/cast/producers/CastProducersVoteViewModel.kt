package com.memtrip.eosreach.app.account.vote.cast.producers

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class CastProducersVoteViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<CastProducersVoteIntent, CastProducersVoteRenderAction, CastProducersVoteViewState>(
    CastProducersVoteViewState(view = CastProducersVoteViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: CastProducersVoteIntent): Observable<CastProducersVoteRenderAction> = when (intent) {
        is CastProducersVoteIntent.Init -> Observable.just(CastProducersVoteRenderAction.OnProgress)
    }

    override fun reducer(previousState: CastProducersVoteViewState, renderAction: CastProducersVoteRenderAction): CastProducersVoteViewState = when (renderAction) {
        CastProducersVoteRenderAction.OnProgress -> previousState.copy(view = CastProducersVoteViewState.View.OnProgress)
        CastProducersVoteRenderAction.OnError -> previousState.copy(view = CastProducersVoteViewState.View.OnError)
    }

    override fun filterIntents(intents: Observable<CastProducersVoteIntent>): Observable<CastProducersVoteIntent> = Observable.merge(
        intents.ofType(CastProducersVoteIntent.Init::class.java).take(1),
        intents.filter {
            !CastProducersVoteIntent.Init::class.java.isInstance(it)
        }
    )
}