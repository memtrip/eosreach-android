package com.memtrip.eosreach.app.welcome.importkey

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ImportKeyViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<ImportKeyIntent, ImportKeyRenderAction, ImportKeyViewState>(
    ImportKeyViewState(view = ImportKeyViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: ImportKeyIntent): Observable<ImportKeyRenderAction> = when (intent) {
        is ImportKeyIntent.Init -> Observable.just(ImportKeyRenderAction.OnProgress)
    }

    override fun reducer(previousState: ImportKeyViewState, renderAction: ImportKeyRenderAction) = when (renderAction) {
        ImportKeyRenderAction.OnProgress -> previousState.copy(view = ImportKeyViewState.View.OnProgress)
        ImportKeyRenderAction.OnError -> previousState.copy(view = ImportKeyViewState.View.OnError)
    }

    override fun filterIntents(intents: Observable<ImportKeyIntent>): Observable<ImportKeyIntent> = Observable.merge(
        intents.ofType(ImportKeyIntent.Init::class.java).take(1),
        intents.filter {
            !ImportKeyIntent.Init::class.java.isInstance(it)
        }
    )
}