package com.memtrip.eosreach.app.settings.viewprivatekeys

import android.app.Application
import com.memtrip.eosreach.utils.RxSchedulers
import com.memtrip.eosreach.wallet.EosKeyManager
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ViewPrivateKeysViewModel @Inject internal constructor(
    private val keyManager: EosKeyManager,
    private val rxSchedulers: RxSchedulers,
    application: Application
) : MxViewModel<ViewPrivateKeysIntent, ViewPrivateKeysRenderAction, ViewPrivateKeysViewState>(
    ViewPrivateKeysViewState(view = ViewPrivateKeysViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: ViewPrivateKeysIntent): Observable<ViewPrivateKeysRenderAction> = when (intent) {
        is ViewPrivateKeysIntent.Init -> Observable.just(ViewPrivateKeysRenderAction.Idle)
        ViewPrivateKeysIntent.DecryptPrivateKeys -> showPrivateKeys()
    }

    override fun reducer(previousState: ViewPrivateKeysViewState, renderAction: ViewPrivateKeysRenderAction): ViewPrivateKeysViewState = when (renderAction) {
        ViewPrivateKeysRenderAction.Idle -> previousState.copy(
            view = ViewPrivateKeysViewState.View.Idle)
        ViewPrivateKeysRenderAction.OnProgress -> previousState.copy(
            view = ViewPrivateKeysViewState.View.OnProgress)
        is ViewPrivateKeysRenderAction.ShowPrivateKeys -> previousState.copy(
            view = ViewPrivateKeysViewState.View.ShowPrivateKeys(renderAction.privateKeys))
        ViewPrivateKeysRenderAction.NoPrivateKeys -> previousState.copy(
            view = ViewPrivateKeysViewState.View.NoPrivateKeys)
    }

    override fun filterIntents(intents: Observable<ViewPrivateKeysIntent>): Observable<ViewPrivateKeysIntent> = Observable.merge(
        intents.ofType(ViewPrivateKeysIntent.Init::class.java).take(1),
        intents.filter {
            !ViewPrivateKeysIntent.Init::class.java.isInstance(it)
        }
    )

    private fun showPrivateKeys(): Observable<ViewPrivateKeysRenderAction> {
        return keyManager.getPrivateKeys().map<ViewPrivateKeysRenderAction> {
            ViewPrivateKeysRenderAction.ShowPrivateKeys(it)
        }
            .onErrorReturn { ViewPrivateKeysRenderAction.NoPrivateKeys }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
            .toObservable()
            .startWith(ViewPrivateKeysRenderAction.OnProgress)
    }
}