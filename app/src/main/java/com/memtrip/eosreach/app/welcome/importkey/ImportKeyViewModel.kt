package com.memtrip.eosreach.app.welcome.importkey

import android.app.Application
import com.memtrip.eosreach.storage.EosReachSharedPreferences
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ImportKeyViewModel @Inject internal constructor(
    private val eosReachSharedPreferences: EosReachSharedPreferences,
    application: Application
) : MxViewModel<ImportKeyIntent, ImportKeyRenderAction, ImportKeyViewState>(
    ImportKeyViewState(view = ImportKeyViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: ImportKeyIntent): Observable<ImportKeyRenderAction> = when (intent) {
        is ImportKeyIntent.Init -> Observable.just(ImportKeyRenderAction.Idle)
        is ImportKeyIntent.ImportKey -> importKey()
    }

    override fun reducer(previousState: ImportKeyViewState, renderAction: ImportKeyRenderAction): ImportKeyViewState = when (renderAction) {
        ImportKeyRenderAction.Idle -> previousState.copy(view = ImportKeyViewState.View.Idle)
        ImportKeyRenderAction.OnProgress -> previousState.copy(view = ImportKeyViewState.View.OnProgress)
        ImportKeyRenderAction.OnSuccess -> previousState.copy(view = ImportKeyViewState.View.OnSuccess)
        ImportKeyRenderAction.OnError -> previousState.copy(view = ImportKeyViewState.View.OnError)
    }

    override fun filterIntents(intents: Observable<ImportKeyIntent>): Observable<ImportKeyIntent> = Observable.merge(
        intents.ofType(ImportKeyIntent.Init::class.java).take(1),
        intents.filter {
            !ImportKeyIntent.Init::class.java.isInstance(it)
        }
    )

    private fun importKey(): Observable<ImportKeyRenderAction> {
        return eosReachSharedPreferences
            .saveAccountCreated()
            .toSingleDefault<ImportKeyRenderAction>(ImportKeyRenderAction.OnSuccess)
            .onErrorReturn { ImportKeyRenderAction.OnError }
            .toObservable()
            .startWith(ImportKeyRenderAction.OnProgress)
    }
}