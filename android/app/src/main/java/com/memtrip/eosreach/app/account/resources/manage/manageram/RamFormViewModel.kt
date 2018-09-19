package com.memtrip.eosreach.app.account.resources.manage.manageram

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable

abstract class RamFormViewModel(
    application: Application
) : MxViewModel<RamFormIntent, RamFormRenderAction, RamFormViewState>(
    RamFormViewState(view = RamFormViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: RamFormIntent): Observable<RamFormRenderAction> = when (intent) {
        is RamFormIntent.Init -> Observable.just(RamFormRenderAction.Idle)
        RamFormIntent.Idle -> Observable.just(RamFormRenderAction.Idle)
        is RamFormIntent.Commit -> commit()
    }

    override fun reducer(previousState: RamFormViewState, renderAction: RamFormRenderAction): RamFormViewState = when (renderAction) {
        RamFormRenderAction.Idle -> previousState.copy(
            view = RamFormViewState.View.Idle)
        RamFormRenderAction.OnProgress -> previousState.copy(
            view = RamFormViewState.View.OnProgress)
        is RamFormRenderAction.OnError -> previousState.copy(
            view = RamFormViewState.View.OnError(renderAction.message, renderAction.log))
        is RamFormRenderAction.OnSuccess -> previousState.copy(
            view = RamFormViewState.View.OnSuccess(renderAction.transactionId))
    }

    override fun filterIntents(intents: Observable<RamFormIntent>): Observable<RamFormIntent> = Observable.merge(
        intents.ofType(RamFormIntent.Init::class.java).take(1),
        intents.filter {
            !RamFormIntent.Init::class.java.isInstance(it)
        }
    )

    private fun commit(): Observable<RamFormRenderAction> {
        return Observable.just(RamFormRenderAction.OnProgress)
    }
}