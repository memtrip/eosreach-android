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
package com.memtrip.eosreach.app.account.actions

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ViewTransferActionViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<ViewTransferActionIntent, ViewTransferActionRenderAction, ViewTransferActionViewState>(
    ViewTransferActionViewState(view = ViewTransferActionViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: ViewTransferActionIntent): Observable<ViewTransferActionRenderAction> = when (intent) {
        is ViewTransferActionIntent.Init -> Observable.just(ViewTransferActionRenderAction.Populate(intent.accountAction))
        ViewTransferActionIntent.Idle -> Observable.just(ViewTransferActionRenderAction.Idle)
        is ViewTransferActionIntent.ViewTransactionBlockExplorer -> Observable.just(ViewTransferActionRenderAction.ViewTransactionBlockExplorer(intent.transactionId))
    }

    override fun reducer(previousState: ViewTransferActionViewState, renderAction: ViewTransferActionRenderAction): ViewTransferActionViewState = when (renderAction) {
        is ViewTransferActionRenderAction.Populate -> previousState.copy(
            view = ViewTransferActionViewState.View.Populate(renderAction.transferAccountAction))
        ViewTransferActionRenderAction.Idle -> previousState.copy(
            view = ViewTransferActionViewState.View.Idle)
        is ViewTransferActionRenderAction.ViewTransactionBlockExplorer -> previousState.copy(
            view = ViewTransferActionViewState.View.ViewTransactionBlockExplorer(renderAction.transactionId))
    }

    override fun filterIntents(intents: Observable<ViewTransferActionIntent>): Observable<ViewTransferActionIntent> = Observable.merge(
        intents.ofType(ViewTransferActionIntent.Init::class.java).take(1),
        intents.filter {
            !ViewTransferActionIntent.Init::class.java.isInstance(it)
        }
    )
}