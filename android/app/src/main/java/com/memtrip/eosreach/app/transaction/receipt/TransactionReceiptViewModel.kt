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
package com.memtrip.eosreach.app.transaction.receipt

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class TransactionReceiptViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<TransactionReceiptIntent, TransferReceiptRenderAction, TransactionReceiptViewState>(
    TransactionReceiptViewState(view = TransactionReceiptViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: TransactionReceiptIntent): Observable<TransferReceiptRenderAction> = when (intent) {
        TransactionReceiptIntent.Idle -> Observable.just(TransferReceiptRenderAction.Idle)
        is TransactionReceiptIntent.Init -> Observable.just(TransferReceiptRenderAction.Populate(intent.actionReceipt))
        is TransactionReceiptIntent.NavigateToBlockExplorer -> Observable.just(TransferReceiptRenderAction.NavigateToBlockExplorer(intent.transactionId))
        TransactionReceiptIntent.NavigateToActions -> Observable.just(TransferReceiptRenderAction.NavigateToActions)
        is TransactionReceiptIntent.NavigateToAccount -> Observable.just(TransferReceiptRenderAction.NavigateToAccount(intent.page))
    }

    override fun reducer(previousState: TransactionReceiptViewState, renderAction: TransferReceiptRenderAction): TransactionReceiptViewState = when (renderAction) {
        TransferReceiptRenderAction.Idle -> previousState.copy(
            view = TransactionReceiptViewState.View.Idle)
        is TransferReceiptRenderAction.Populate -> previousState.copy(
            view = TransactionReceiptViewState.View.Populate(renderAction.actionReceipt))
        is TransferReceiptRenderAction.NavigateToBlockExplorer -> previousState.copy(
            view = TransactionReceiptViewState.View.NavigateToBlockExplorer(renderAction.transactionId))
        TransferReceiptRenderAction.NavigateToActions -> previousState.copy(
            view = TransactionReceiptViewState.View.NavigateToActions)
        is TransferReceiptRenderAction.NavigateToAccount -> previousState.copy(
            view = TransactionReceiptViewState.View.NavigateToAccount(renderAction.page))
    }

    override fun filterIntents(intents: Observable<TransactionReceiptIntent>): Observable<TransactionReceiptIntent> = Observable.merge(
        intents.ofType(TransactionReceiptIntent.Init::class.java).take(1),
        intents.filter {
            !TransactionReceiptIntent.Init::class.java.isInstance(it)
        }
    )
}