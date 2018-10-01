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
package com.memtrip.eosreach.app.transaction.log

import android.app.Application
import com.memtrip.eosreach.db.transaction.GetTransactionLogs
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ViewConfirmedTransactionsViewModel @Inject internal constructor(
    private val getTransactionLogs: GetTransactionLogs,
    application: Application
) : MxViewModel<ViewConfirmedTransactionsIntent, ViewConfirmedTransactionsRenderAction, ViewConfirmedTransactionsViewState>(
    ViewConfirmedTransactionsViewState(view = ViewConfirmedTransactionsViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: ViewConfirmedTransactionsIntent): Observable<ViewConfirmedTransactionsRenderAction> = when (intent) {
        is ViewConfirmedTransactionsIntent.Init -> getLogs()
        ViewConfirmedTransactionsIntent.Idle -> Observable.just(ViewConfirmedTransactionsRenderAction.Idle)
        is ViewConfirmedTransactionsIntent.NavigateToBlockExplorer -> Observable.just(ViewConfirmedTransactionsRenderAction.NavigateToBlockExplorer(intent.transactionId))
    }

    override fun reducer(previousState: ViewConfirmedTransactionsViewState, renderAction: ViewConfirmedTransactionsRenderAction): ViewConfirmedTransactionsViewState = when (renderAction) {
        ViewConfirmedTransactionsRenderAction.Idle -> previousState.copy(
            view = ViewConfirmedTransactionsViewState.View.Idle)
        ViewConfirmedTransactionsRenderAction.OnProgress -> previousState.copy(
            view = ViewConfirmedTransactionsViewState.View.OnProgress)
        ViewConfirmedTransactionsRenderAction.OnError -> previousState.copy(
            view = ViewConfirmedTransactionsViewState.View.OnError)
        is ViewConfirmedTransactionsRenderAction.Populate -> previousState.copy(
            view = ViewConfirmedTransactionsViewState.View.Populate(renderAction.transactionLogEntities))
        is ViewConfirmedTransactionsRenderAction.NavigateToBlockExplorer -> previousState.copy(
            view = ViewConfirmedTransactionsViewState.View.NavigateToBlockExplorer(renderAction.transactionId))
    }

    override fun filterIntents(intents: Observable<ViewConfirmedTransactionsIntent>): Observable<ViewConfirmedTransactionsIntent> = Observable.merge(
        intents.ofType(ViewConfirmedTransactionsIntent.Init::class.java).take(1),
        intents.filter {
            !ViewConfirmedTransactionsIntent.Init::class.java.isInstance(it)
        }
    )

    private fun getLogs(): Observable<ViewConfirmedTransactionsRenderAction> {
        return getTransactionLogs.select().map<ViewConfirmedTransactionsRenderAction> {
            ViewConfirmedTransactionsRenderAction.Populate(it)
        }.onErrorReturn {
            ViewConfirmedTransactionsRenderAction.OnError
        }.toObservable().startWith(ViewConfirmedTransactionsRenderAction.OnProgress)
    }
}