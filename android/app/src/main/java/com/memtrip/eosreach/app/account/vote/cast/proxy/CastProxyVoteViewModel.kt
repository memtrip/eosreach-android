package com.memtrip.eosreach.app.account.vote.cast.proxy

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class CastProxyVoteViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<CastProxyVoteIntent, CastProxyVoteRenderAction, CastProxyVoteViewState>(
    CastProxyVoteViewState(view = CastProxyVoteViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: CastProxyVoteIntent): Observable<CastProxyVoteRenderAction> = when (intent) {
        is CastProxyVoteIntent.Init -> Observable.just(CastProxyVoteRenderAction.OnProgress)
    }

    override fun reducer(previousState: CastProxyVoteViewState, renderAction: CastProxyVoteRenderAction): CastProxyVoteViewState = when (renderAction) {
        CastProxyVoteRenderAction.OnProgress -> previousState.copy(view = CastProxyVoteViewState.View.OnProgress)
        CastProxyVoteRenderAction.OnError -> previousState.copy(view = CastProxyVoteViewState.View.OnError)
    }

    override fun filterIntents(intents: Observable<CastProxyVoteIntent>): Observable<CastProxyVoteIntent> = Observable.merge(
        intents.ofType(CastProxyVoteIntent.Init::class.java).take(1),
        intents.filter {
            !CastProxyVoteIntent.Init::class.java.isInstance(it)
        }
    )
}