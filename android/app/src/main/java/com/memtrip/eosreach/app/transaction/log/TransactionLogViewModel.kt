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