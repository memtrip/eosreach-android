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
package com.memtrip.eosreach.app.explore

import android.app.Application
import com.memtrip.eosreach.api.blockproducer.RegisteredBlockProducerError
import com.memtrip.eosreach.api.blockproducer.RegisteredBlockProducerRequest
import com.memtrip.eosreach.api.blockproducer.RegisteredBlockProducerRequestImpl
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class RegisteredBlockProducersViewModel @Inject internal constructor(
    private val registeredBlockProducerRequest: RegisteredBlockProducerRequest,
    application: Application
) : MxViewModel<RegisteredBlockProducersIntent, RegisteredBlockProducersRenderAction, RegisteredBlockProducersViewState>(
    RegisteredBlockProducersViewState(view = RegisteredBlockProducersViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: RegisteredBlockProducersIntent): Observable<RegisteredBlockProducersRenderAction> = when (intent) {
        RegisteredBlockProducersIntent.Idle -> Observable.just(RegisteredBlockProducersRenderAction.Idle)
        is RegisteredBlockProducersIntent.Init -> getRegisteredBlockProducers()
        RegisteredBlockProducersIntent.Retry -> getRegisteredBlockProducers()
        is RegisteredBlockProducersIntent.LoadMore -> getRegisteredBlockProducers(intent.lastAccountName)
        is RegisteredBlockProducersIntent.WebsiteSelected -> Observable.just(RegisteredBlockProducersRenderAction.WebsiteSelected(intent.website))
        is RegisteredBlockProducersIntent.RegisteredBlockProducersSelected -> Observable.just(RegisteredBlockProducersRenderAction.RegisteredBlockProducersSelected(intent.accountName))
    }

    override fun reducer(previousState: RegisteredBlockProducersViewState, renderAction: RegisteredBlockProducersRenderAction): RegisteredBlockProducersViewState = when (renderAction) {
        RegisteredBlockProducersRenderAction.Idle -> previousState.copy(view = RegisteredBlockProducersViewState.View.Idle)
        RegisteredBlockProducersRenderAction.Empty -> previousState.copy(view = RegisteredBlockProducersViewState.View.Empty)
        is RegisteredBlockProducersRenderAction.OnSuccess -> previousState.copy(view = RegisteredBlockProducersViewState.View.OnSuccess(renderAction.registeredBlockProducers))
        RegisteredBlockProducersRenderAction.OnProgress -> previousState.copy(view = RegisteredBlockProducersViewState.View.OnProgress)
        RegisteredBlockProducersRenderAction.OnError -> previousState.copy(view = RegisteredBlockProducersViewState.View.OnError)
        RegisteredBlockProducersRenderAction.OnLoadMoreProgress -> previousState.copy(view = RegisteredBlockProducersViewState.View.OnLoadMoreProgress)
        RegisteredBlockProducersRenderAction.OnLoadMoreError -> previousState.copy(view = RegisteredBlockProducersViewState.View.OnLoadMoreError)
        is RegisteredBlockProducersRenderAction.WebsiteSelected -> previousState.copy(view = RegisteredBlockProducersViewState.View.WebsiteSelected(renderAction.url))
        is RegisteredBlockProducersRenderAction.RegisteredBlockProducersSelected -> previousState.copy(view = RegisteredBlockProducersViewState.View.RegisteredBlockProducersSelected(renderAction.accountName))
    }

    override fun filterIntents(intents: Observable<RegisteredBlockProducersIntent>): Observable<RegisteredBlockProducersIntent> = Observable.merge(
        intents.ofType(RegisteredBlockProducersIntent.Init::class.java).take(1),
        intents.filter {
            !RegisteredBlockProducersIntent.Init::class.java.isInstance(it)
        }
    )

    private fun getRegisteredBlockProducers(nextAccount: String = ""): Observable<RegisteredBlockProducersRenderAction> {
        return registeredBlockProducerRequest.getProducers(200, nextAccount).map { result ->
            if (result.success) {
                RegisteredBlockProducersRenderAction.OnSuccess(result.data!!)
            } else {
                getProducersError(result.apiError!!, nextAccount)
            }
        }.toObservable().startWith(getProducersProgress(nextAccount))
    }

    private fun getProducersError(
        registeredBlockProducerError: RegisteredBlockProducerError,
        nextAccount: String
    ): RegisteredBlockProducersRenderAction = when (registeredBlockProducerError) {
        RegisteredBlockProducerError.Empty -> RegisteredBlockProducersRenderAction.Empty
        RegisteredBlockProducerError.GenericError -> {
            if (nextAccount.isNotEmpty()) {
                RegisteredBlockProducersRenderAction.OnLoadMoreError
            } else {
                RegisteredBlockProducersRenderAction.OnError
            }
        }
    }

    private fun getProducersProgress(nextAccount: String): RegisteredBlockProducersRenderAction {
        return if (nextAccount.isNotEmpty()) {
            RegisteredBlockProducersRenderAction.OnLoadMoreProgress
        } else {
            RegisteredBlockProducersRenderAction.OnProgress
        }
    }
}