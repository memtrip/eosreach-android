package com.memtrip.eosreach.app.account.vote

import android.app.Application
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
        is VoteIntent.Init -> Observable.just(VoteRenderAction.OnProgress)
    }

    override fun reducer(previousState: VoteViewState, renderAction: VoteRenderAction): VoteViewState = when (renderAction) {
        VoteRenderAction.OnProgress -> previousState.copy(view = VoteViewState.View.OnProgress)
        VoteRenderAction.OnError -> previousState.copy(view = VoteViewState.View.OnError)
    }

    override fun filterIntents(intents: Observable<VoteIntent>): Observable<VoteIntent> = Observable.merge(
        intents.ofType(VoteIntent.Init::class.java).take(1),
        intents.filter {
            !VoteIntent.Init::class.java.isInstance(it)
        }
    )
}