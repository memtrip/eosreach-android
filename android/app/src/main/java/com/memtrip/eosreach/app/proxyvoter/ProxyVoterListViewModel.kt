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
import com.memtrip.eosreach.api.proxyvoter.ProxyVoterRequest
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ProxyVoterListViewModel @Inject internal constructor(
    private val proxyVoterRequest: ProxyVoterRequest,
    application: Application
) : MxViewModel<ProxyVoterListIntent, ProxyVoterListRenderAction, ProxyVoterListViewState>(
    ProxyVoterListViewState(view = ProxyVoterListViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: ProxyVoterListIntent): Observable<ProxyVoterListRenderAction> = when (intent) {
        ProxyVoterListIntent.Idle -> Observable.just(ProxyVoterListRenderAction.Idle)
        ProxyVoterListIntent.Init -> getProxyVoters()
        ProxyVoterListIntent.Retry -> getProxyVoters()
        is ProxyVoterListIntent.ProxyVoterInformationSelected -> Observable.just(ProxyVoterListRenderAction.ProxyInformationSelected(intent.proxyVoterDetails))
        is ProxyVoterListIntent.LoadMoreProxyVoters -> getMoreProxyVoters(intent.lastAccount)
        is ProxyVoterListIntent.ProxyVoterSelected -> Observable.just(ProxyVoterListRenderAction.ProxyVoterSelected(intent.proxyVoterDetails))
    }

    override fun reducer(previousState: ProxyVoterListViewState, renderAction: ProxyVoterListRenderAction): ProxyVoterListViewState = when (renderAction) {
        ProxyVoterListRenderAction.Idle -> previousState.copy(view = ProxyVoterListViewState.View.Idle)
        ProxyVoterListRenderAction.OnProgress -> previousState.copy(view = ProxyVoterListViewState.View.OnProgress)
        ProxyVoterListRenderAction.OnError -> previousState.copy(view = ProxyVoterListViewState.View.OnError)
        is ProxyVoterListRenderAction.OnSuccess -> previousState.copy(view = ProxyVoterListViewState.View.OnSuccess(renderAction.proxyVoterDetails))
        is ProxyVoterListRenderAction.OnMoreSuccess -> previousState.copy(view = ProxyVoterListViewState.View.OnMoreSuccess(renderAction.proxyVoterDetails))
        is ProxyVoterListRenderAction.ProxyVoterSelected -> previousState.copy(view = ProxyVoterListViewState.View.ProxyVoterSelected(renderAction.proxyVoterDetails))
        is ProxyVoterListRenderAction.ProxyInformationSelected -> previousState.copy(view = ProxyVoterListViewState.View.ProxyVoterInformationSelected(renderAction.proxyVoterDetails))
        ProxyVoterListRenderAction.OnMoreError -> previousState.copy(view = ProxyVoterListViewState.View.OnMoreError)
        ProxyVoterListRenderAction.OnMoreProgress -> previousState.copy(view = ProxyVoterListViewState.View.OnMoreProgress)
    }

    override fun filterIntents(intents: Observable<ProxyVoterListIntent>): Observable<ProxyVoterListIntent> = Observable.merge(
        intents.ofType(ProxyVoterListIntent.Init::class.java).take(1),
        intents.filter {
            !ProxyVoterListIntent.Init::class.java.isInstance(it)
        }
    )

    private fun getProxyVoters(): Observable<ProxyVoterListRenderAction> {
        return proxyVoterRequest.getProxyVoters().map { result ->
            if (result.success) {
                ProxyVoterListRenderAction.OnSuccess(result.data!!)
            } else {
                ProxyVoterListRenderAction.OnError
            }
        }.onErrorReturn {
            ProxyVoterListRenderAction.OnError
        }.toObservable().startWith(ProxyVoterListRenderAction.OnProgress)
    }

    private fun getMoreProxyVoters(nextAccount: String): Observable<ProxyVoterListRenderAction> {
        return proxyVoterRequest.getProxyVoters(nextAccount).map { result ->
            if (result.success) {
                ProxyVoterListRenderAction.OnSuccess(result.data!!)
            } else {
                ProxyVoterListRenderAction.OnMoreError
            }
        }.onErrorReturn {
            ProxyVoterListRenderAction.OnMoreError
        }.toObservable().startWith(ProxyVoterListRenderAction.OnMoreProgress)
    }
}