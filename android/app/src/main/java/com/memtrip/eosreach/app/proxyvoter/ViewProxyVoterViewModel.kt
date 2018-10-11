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
package com.memtrip.eosreach.app.proxyvoter

import android.app.Application
import android.net.Uri
import com.memtrip.eosreach.api.proxyvoter.ProxyVoterRequest
import com.memtrip.eosreach.app.blockproducer.ViewBlockProducerRenderAction
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ViewProxyVoterViewModel @Inject internal constructor(
    private val proxyVoterRequest: ProxyVoterRequest,
    application: Application
) : MxViewModel<ViewProxyVoterIntent, ViewProxyVoterRenderAction, ViewProxyVoterViewState>(
    ViewProxyVoterViewState(view = ViewProxyVoterViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: ViewProxyVoterIntent): Observable<ViewProxyVoterRenderAction> = when (intent) {
        ViewProxyVoterIntent.Idle -> Observable.just(ViewProxyVoterRenderAction.Idle)
        is ViewProxyVoterIntent.InitWithDetails -> Observable.just(ViewProxyVoterRenderAction.Populate(intent.proxyVoterDetails))
        is ViewProxyVoterIntent.InitWithName -> getProxyVoterByAccountName(intent.proxyVoterName)
        is ViewProxyVoterIntent.NavigateToUrl -> Observable.just(validateUrl(intent.url))
        is ViewProxyVoterIntent.Retry -> getProxyVoterByAccountName(intent.proxyVoterName)
    }

    override fun reducer(previousState: ViewProxyVoterViewState, renderAction: ViewProxyVoterRenderAction): ViewProxyVoterViewState = when (renderAction) {
        ViewProxyVoterRenderAction.Idle -> previousState.copy(view = ViewProxyVoterViewState.View.Idle)
        ViewProxyVoterRenderAction.OnProgress -> previousState.copy(view = ViewProxyVoterViewState.View.OnProgress)
        ViewProxyVoterRenderAction.OnError -> previousState.copy(view = ViewProxyVoterViewState.View.OnError)
        is ViewProxyVoterRenderAction.Populate -> previousState.copy(view = ViewProxyVoterViewState.View.Populate(renderAction.proxyVoterDetails))
        is ViewProxyVoterRenderAction.OnInvalidUrl -> previousState.copy(view = ViewProxyVoterViewState.View.OnInvalidUrl(renderAction.url))
        is ViewProxyVoterRenderAction.NavigateToUrl -> previousState.copy(view = ViewProxyVoterViewState.View.NavigateToUrl(renderAction.url))
    }

    override fun filterIntents(intents: Observable<ViewProxyVoterIntent>): Observable<ViewProxyVoterIntent> = Observable.merge(
        intents.ofType(ViewProxyVoterIntent.InitWithDetails::class.java).take(1),
        intents.filter {
            !ViewProxyVoterIntent.InitWithDetails::class.java.isInstance(it)
        }
    )

    private fun validateUrl(url: String): ViewProxyVoterRenderAction {
        return if (url.trim().isNotEmpty() && checkParseUrl(url.trim())) {
            ViewProxyVoterRenderAction.NavigateToUrl(url.trim())
        } else {
            ViewProxyVoterRenderAction.OnInvalidUrl(url)
        }
    }

    private fun checkParseUrl(url: String): Boolean {
        return try {
            Uri.parse(url)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun getProxyVoterByAccountName(accountName: String): Observable<ViewProxyVoterRenderAction> {
        return proxyVoterRequest.getProxy(accountName).map { result ->
            if (result.success) {
                ViewProxyVoterRenderAction.Populate(result.data!!)
            } else {
                ViewProxyVoterRenderAction.OnError
            }
        }.toObservable().startWith(ViewProxyVoterRenderAction.OnProgress)
    }
}