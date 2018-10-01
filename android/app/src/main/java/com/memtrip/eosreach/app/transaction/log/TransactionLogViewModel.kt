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
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class TransactionLogViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<TransactionLogIntent, TransactionLogRenderAction, TransactionLogViewState>(
    TransactionLogViewState(view = TransactionLogViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: TransactionLogIntent): Observable<TransactionLogRenderAction> = when (intent) {
        is TransactionLogIntent.Init -> Observable.just(TransactionLogRenderAction.Idle)
        is TransactionLogIntent.ShowLog -> Observable.just(TransactionLogRenderAction.ShowLog(intent.log))
    }

    override fun reducer(previousState: TransactionLogViewState, renderAction: TransactionLogRenderAction): TransactionLogViewState = when (renderAction) {
        TransactionLogRenderAction.Idle -> previousState.copy(view = TransactionLogViewState.View.Idle)
        is TransactionLogRenderAction.ShowLog -> previousState.copy(view = TransactionLogViewState.View.ShowLog(renderAction.log))
    }

    override fun filterIntents(intents: Observable<TransactionLogIntent>): Observable<TransactionLogIntent> = Observable.merge(
        intents.ofType(TransactionLogIntent.Init::class.java).take(1),
        intents.filter {
            !TransactionLogIntent.Init::class.java.isInstance(it)
        }
    )
}