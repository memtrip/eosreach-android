package com.memtrip.eosreach.app.welcome.splash

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class SplashViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<SplashIntent, SplashRenderAction, SplashViewState>(
    SplashViewState(view = SplashViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: SplashIntent): Observable<SplashRenderAction> = when (intent) {
        is SplashIntent.Init -> Observable.just(SplashRenderAction.OnProgress)
    }

    override fun reducer(previousState: SplashViewState, renderAction: SplashRenderAction) = when (renderAction) {
        SplashRenderAction.OnProgress -> previousState.copy(view = SplashViewState.View.OnProgress)
        SplashRenderAction.OnError -> previousState.copy(view = SplashViewState.View.OnError)
    }

    override fun filterIntents(intents: Observable<SplashIntent>): Observable<SplashIntent> = Observable.merge(
        intents.ofType(SplashIntent.Init::class.java).take(1),
        intents.filter {
            !SplashIntent.Init::class.java.isInstance(it)
        }
    )
}