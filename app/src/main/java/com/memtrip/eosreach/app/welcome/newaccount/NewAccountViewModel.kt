package com.memtrip.eosreach.app.welcome.newaccount

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class NewAccountViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<NewAccountIntent, NewAccountRenderAction, NewAccountViewState>(
    NewAccountViewState(view = NewAccountViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: NewAccountIntent): Observable<NewAccountRenderAction> = when (intent) {
        is NewAccountIntent.Init -> Observable.just(NewAccountRenderAction.OnProgress)
    }

    override fun reducer(previousState: NewAccountViewState, renderAction: NewAccountRenderAction) = when (renderAction) {
        NewAccountRenderAction.OnProgress -> previousState.copy(view = NewAccountViewState.View.OnProgress)
        NewAccountRenderAction.OnError -> previousState.copy(view = NewAccountViewState.View.OnError)
    }

    override fun filterIntents(intents: Observable<NewAccountIntent>): Observable<NewAccountIntent> = Observable.merge(
        intents.ofType(NewAccountIntent.Init::class.java).take(1),
        intents.filter {
            !NewAccountIntent.Init::class.java.isInstance(it)
        }
    )
}