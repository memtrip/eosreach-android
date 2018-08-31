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
        SplashIntent.Init -> Observable.just(SplashRenderAction.Idle)
        SplashIntent.NavigateToCreateAccount -> Observable.just(SplashRenderAction.NavigateToCreateAccount)
        SplashIntent.NavigateToImportKeys -> Observable.just(SplashRenderAction.NavigateToImportKey)
    }

    override fun reducer(previousState: SplashViewState, renderAction: SplashRenderAction): SplashViewState = when (renderAction) {
        SplashRenderAction.Idle -> previousState.copy(view = SplashViewState.View.Idle)
        SplashRenderAction.NavigateToCreateAccount -> previousState.copy(view = SplashViewState.View.NavigateToCreateAccount)
        SplashRenderAction.NavigateToImportKey -> previousState.copy(view = SplashViewState.View.NavigateToImportKeys)
    }
}