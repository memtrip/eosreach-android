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