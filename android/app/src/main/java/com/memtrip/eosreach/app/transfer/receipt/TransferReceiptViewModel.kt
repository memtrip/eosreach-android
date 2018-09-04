package com.memtrip.eosreach.app.transfer.receipt

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class TransferReceiptViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<TransferReceiptIntent, TransferReceiptRenderAction, TransferReceiptViewState>(
    TransferReceiptViewState(view = TransferReceiptViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: TransferReceiptIntent): Observable<TransferReceiptRenderAction> = when (intent) {
        is TransferReceiptIntent.Init -> Observable.just(TransferReceiptRenderAction.OnProgress)
    }

    override fun reducer(previousState: TransferReceiptViewState, renderAction: TransferReceiptRenderAction): TransferReceiptViewState = when (renderAction) {
        TransferReceiptRenderAction.OnProgress -> previousState.copy(view = TransferReceiptViewState.View.OnProgress)
        TransferReceiptRenderAction.OnError -> previousState.copy(view = TransferReceiptViewState.View.OnError)
    }

    override fun filterIntents(intents: Observable<TransferReceiptIntent>): Observable<TransferReceiptIntent> = Observable.merge(
        intents.ofType(TransferReceiptIntent.Init::class.java).take(1),
        intents.filter {
            !TransferReceiptIntent.Init::class.java.isInstance(it)
        }
    )
}