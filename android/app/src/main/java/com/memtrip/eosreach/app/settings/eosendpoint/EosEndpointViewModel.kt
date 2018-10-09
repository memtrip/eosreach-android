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
package com.memtrip.eosreach.app.settings.eosendpoint

import android.app.Application
import com.memtrip.eosreach.R
import com.memtrip.eosreach.db.sharedpreferences.EosEndpoint
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import java.net.MalformedURLException
import java.net.URL
import javax.inject.Inject

class EosEndpointViewModel @Inject internal constructor(
    private val eosEndpointUseCase: EosEndpointUseCase,
    private val eosEndPoint: EosEndpoint,
    application: Application
) : MxViewModel<EosEndpointIntent, EosEndpointRenderAction, EosEndpointViewState>(
    EosEndpointViewState(
        endpointUrl = eosEndPoint.get(),
        view = EosEndpointViewState.View.Idle
    ),
    application
) {

    override fun dispatcher(intent: EosEndpointIntent): Observable<EosEndpointRenderAction> = when (intent) {
        EosEndpointIntent.Idle -> Observable.just(EosEndpointRenderAction.Idle)
        is EosEndpointIntent.Init -> Observable.just(EosEndpointRenderAction.Idle)
        is EosEndpointIntent.ChangeEndpoint -> changeEndpoint(intent.endpoint)
        EosEndpointIntent.NavigateToBlockProducerList -> Observable.just(EosEndpointRenderAction.NavigateToBlockProducerList)
    }

    override fun reducer(previousState: EosEndpointViewState, renderAction: EosEndpointRenderAction): EosEndpointViewState = when (renderAction) {
        EosEndpointRenderAction.Idle -> previousState.copy(
            view = EosEndpointViewState.View.Idle)
        EosEndpointRenderAction.OnProgress -> previousState.copy(
            view = EosEndpointViewState.View.OnProgress)
        is EosEndpointRenderAction.OnError -> previousState.copy(
            view = EosEndpointViewState.View.OnError(renderAction.message))
        EosEndpointRenderAction.OnSuccess -> previousState.copy(
            view = EosEndpointViewState.View.OnSuccess)
        EosEndpointRenderAction.NavigateToBlockProducerList -> previousState.copy(
            view = EosEndpointViewState.View.NavigateToBlockProducerList)
    }

    override fun filterIntents(intents: Observable<EosEndpointIntent>): Observable<EosEndpointIntent> = Observable.merge(
        intents.ofType(EosEndpointIntent.Init::class.java).take(1),
        intents.filter {
            !EosEndpointIntent.Init::class.java.isInstance(it)
        }
    )

    private fun changeEndpoint(endpointUrl: String): Observable<EosEndpointRenderAction> {
        if ((!endpointUrl.startsWith("http://") &&
                !endpointUrl.startsWith("https://")) || !validUrl(endpointUrl)
        ) {
            return Observable.just(EosEndpointRenderAction.OnError(
                context().getString(R.string.eos_endpoint_validation_invalid_url)))
        } else if (endpointUrl == eosEndPoint.get()) {
            return Observable.just(EosEndpointRenderAction.OnError(
                context().getString(R.string.eos_endpoint_validation_nothing_changed, endpointUrl)))
        } else {
            return eosEndpointUseCase.getInfo(endpointUrl)
                .map { result ->
                    if (result.success) {
                        if (!endpointUrl.endsWith("/")) {
                            eosEndPoint.put("$endpointUrl/")
                        } else {
                            eosEndPoint.put(endpointUrl)
                        }
                        EosEndpointRenderAction.OnSuccess
                    } else {
                        EosEndpointRenderAction.OnError(
                            context().getString(R.string.eos_endpoint_validation_generic))
                    }
                }.toObservable().startWith(EosEndpointRenderAction.OnProgress)
        }
    }

    private fun validUrl(url: String): Boolean = try {
        URL(url)
        true
    } catch (e: MalformedURLException) {
        false
    }
}