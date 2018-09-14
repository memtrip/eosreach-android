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