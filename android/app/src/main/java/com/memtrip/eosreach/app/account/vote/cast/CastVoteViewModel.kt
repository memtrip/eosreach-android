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
        CastVoteIntent.Idle -> Observable.just(CastVoteRenderAction.Idle)
        is CastVoteIntent.Init -> Observable.just(CastVoteRenderAction.Populate(intent.eosAccount))
    }

    override fun reducer(previousState: CastVoteViewState, renderAction: CastVoteRenderAction): CastVoteViewState = when (renderAction) {
        CastVoteRenderAction.Idle -> previousState.copy(view = CastVoteViewState.View.Idle)
        is CastVoteRenderAction.Populate -> previousState.copy(view = CastVoteViewState.View.Populate(renderAction.eosAccount))
    }

    override fun filterIntents(intents: Observable<CastVoteIntent>): Observable<CastVoteIntent> = Observable.merge(
        intents.ofType(CastVoteIntent.Init::class.java).take(1),
        intents.filter {
            !CastVoteIntent.Init::class.java.isInstance(it)
        }
    )
}