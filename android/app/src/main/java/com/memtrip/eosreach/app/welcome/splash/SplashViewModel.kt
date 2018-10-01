/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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