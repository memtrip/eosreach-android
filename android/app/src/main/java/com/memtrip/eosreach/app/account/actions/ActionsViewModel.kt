package com.memtrip.eosreach.app.account.actions

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ActionsViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<ActionsIntent, ActionsRenderAction, ActionsViewState>(
    ActionsViewState(view = ActionsViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: ActionsIntent): Observable<ActionsRenderAction> = when (intent) {
        is ActionsIntent.Init -> Observable.just(ActionsRenderAction.OnProgress)
    }

    override fun reducer(previousState: ActionsViewState, renderAction: ActionsRenderAction): ActionsViewState = when (renderAction) {
        ActionsRenderAction.OnProgress -> previousState.copy(view = ActionsViewState.View.OnProgress)
        ActionsRenderAction.OnError -> previousState.copy(view = ActionsViewState.View.OnError)
    }

    override fun filterIntents(intents: Observable<ActionsIntent>): Observable<ActionsIntent> = Observable.merge(
        intents.ofType(ActionsIntent.Init::class.java).take(1),
        intents.filter {
            !ActionsIntent.Init::class.java.isInstance(it)
        }
    )
}