package com.memtrip.eosreach.app.welcome.keyimported

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class KeyImportedViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<KeyImportedIntent, KeyImportedRenderAction, KeyImportedViewState>(
    KeyImportedViewState(view = KeyImportedViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: KeyImportedIntent): Observable<KeyImportedRenderAction> = when (intent) {
        is KeyImportedIntent.Init -> Observable.just(KeyImportedRenderAction.Idle)
        KeyImportedIntent.Done -> Observable.just(KeyImportedRenderAction.Done)
    }

    override fun reducer(previousState: KeyImportedViewState, renderAction: KeyImportedRenderAction): KeyImportedViewState = when (renderAction) {
        KeyImportedRenderAction.Idle -> previousState.copy(view = KeyImportedViewState.View.Idle)
        KeyImportedRenderAction.Done -> previousState.copy(view = KeyImportedViewState.View.Done)
    }

    override fun filterIntents(intents: Observable<KeyImportedIntent>): Observable<KeyImportedIntent> = Observable.merge(
        intents.ofType(KeyImportedIntent.Init::class.java).take(1),
        intents.filter {
            !KeyImportedIntent.Init::class.java.isInstance(it)
        }
    )
}