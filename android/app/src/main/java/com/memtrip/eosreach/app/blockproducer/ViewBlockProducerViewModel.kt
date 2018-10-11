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
package com.memtrip.eosreach.app.blockproducer

import android.app.Application
import android.net.Uri
import com.memtrip.eosreach.api.blockproducer.BlockProducerRequest
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ViewBlockProducerViewModel @Inject internal constructor(
    private val blockProducerRequest: BlockProducerRequest,
    application: Application
) : MxViewModel<ViewBlockProducerIntent, ViewBlockProducerRenderAction, ViewBlockProducerViewState>(
    ViewBlockProducerViewState(view = ViewBlockProducerViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: ViewBlockProducerIntent): Observable<ViewBlockProducerRenderAction> = when (intent) {
        is ViewBlockProducerIntent.InitWithDetails -> Observable.just(ViewBlockProducerRenderAction.Populate(intent.blockProducerDetails))
        is ViewBlockProducerIntent.InitWithName -> getBlockProducerByAccountName(intent.accountName)
        ViewBlockProducerIntent.Idle -> Observable.just(ViewBlockProducerRenderAction.Idle)
        is ViewBlockProducerIntent.NavigateToUrl -> Observable.just(validateUrl(intent.url))
    }

    override fun reducer(previousState: ViewBlockProducerViewState, renderAction: ViewBlockProducerRenderAction): ViewBlockProducerViewState = when (renderAction) {
        ViewBlockProducerRenderAction.Idle -> previousState.copy(view = ViewBlockProducerViewState.View.Idle)
        ViewBlockProducerRenderAction.OnProgress -> previousState.copy(view = ViewBlockProducerViewState.View.OnProgress)
        ViewBlockProducerRenderAction.OnError -> previousState.copy(view = ViewBlockProducerViewState.View.OnError)
        is ViewBlockProducerRenderAction.OnInvalidUrl -> previousState.copy(view = ViewBlockProducerViewState.View.OnInvalidUrl(renderAction.url))
        is ViewBlockProducerRenderAction.NavigateToUrl -> previousState.copy(view = ViewBlockProducerViewState.View.NavigateToUrl(renderAction.url))
        is ViewBlockProducerRenderAction.Populate -> previousState.copy(view = ViewBlockProducerViewState.View.Populate(renderAction.blockProducerDetails))
    }

    override fun filterIntents(intents: Observable<ViewBlockProducerIntent>): Observable<ViewBlockProducerIntent> = Observable.merge(
        intents.ofType(ViewBlockProducerIntent.InitWithDetails::class.java).take(1),
        intents.filter {
            !ViewBlockProducerIntent.InitWithDetails::class.java.isInstance(it)
        }
    )

    private fun validateUrl(url: String): ViewBlockProducerRenderAction {
        return if (checkParseUrl(url)) {
            ViewBlockProducerRenderAction.NavigateToUrl(url)
        } else {
            ViewBlockProducerRenderAction.OnInvalidUrl(url)
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

    private fun getBlockProducerByAccountName(accountName: String): Observable<ViewBlockProducerRenderAction> {
        return blockProducerRequest.getSingleBlockProducer(accountName).map { result ->
            if (result.success) {
                ViewBlockProducerRenderAction.Populate(result.data!!)
            } else {
                ViewBlockProducerRenderAction.OnError
            }
        }.onErrorReturn {
            it.printStackTrace()
            ViewBlockProducerRenderAction.OnError
        }.toObservable().startWith(ViewBlockProducerRenderAction.OnProgress)
    }
}