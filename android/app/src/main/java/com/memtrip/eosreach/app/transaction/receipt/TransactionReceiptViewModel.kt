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
        is TransactionReceiptIntent.Init -> Observable.just(TransferReceiptRenderAction.Populate(intent.transactionReceipt))
        TransactionReceiptIntent.NavigateToActions -> Observable.just(TransferReceiptRenderAction.NavigateToActions)
    }

    override fun reducer(previousState: TransactionReceiptViewState, renderAction: TransferReceiptRenderAction): TransactionReceiptViewState = when (renderAction) {
        is TransferReceiptRenderAction.Populate -> previousState.copy(
            view = TransactionReceiptViewState.View.Populate(renderAction.transactionReceipt))
        TransferReceiptRenderAction.NavigateToActions -> previousState.copy(
            view = TransactionReceiptViewState.View.NavigateToActions)
    }

    override fun filterIntents(intents: Observable<TransactionReceiptIntent>): Observable<TransactionReceiptIntent> = Observable.merge(
        intents.ofType(TransactionReceiptIntent.Init::class.java).take(1),
        intents.filter {
            !TransactionReceiptIntent.Init::class.java.isInstance(it)
        }
    )
}