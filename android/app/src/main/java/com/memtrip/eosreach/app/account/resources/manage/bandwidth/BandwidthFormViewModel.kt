package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

abstract class BandwidthFormViewModel(
    application: Application
) : MxViewModel<BandwidthFormIntent, BandwidthFormRenderAction, BandwidthFormViewState>(
    BandwidthFormViewState(view = BandwidthFormViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: BandwidthFormIntent): Observable<BandwidthFormRenderAction> = when (intent) {
        is BandwidthFormIntent.Init -> Observable.just(BandwidthFormRenderAction.Idle)
        BandwidthFormIntent.Idle -> Observable.just(BandwidthFormRenderAction.Idle)
        is BandwidthFormIntent.Commit -> commit()
    }

    override fun reducer(previousState: BandwidthFormViewState, renderAction: BandwidthFormRenderAction): BandwidthFormViewState = when (renderAction) {
        BandwidthFormRenderAction.Idle -> previousState.copy(
            view = BandwidthFormViewState.View.Idle)
        BandwidthFormRenderAction.OnProgress -> previousState.copy(
            view = BandwidthFormViewState.View.OnProgress)
        is BandwidthFormRenderAction.OnError -> previousState.copy(
            view = BandwidthFormViewState.View.OnError(renderAction.message, renderAction.log))
        is BandwidthFormRenderAction.OnSuccess -> previousState.copy(
            view = BandwidthFormViewState.View.OnSuccess(renderAction.transactionId))
    }

    override fun filterIntents(intents: Observable<BandwidthFormIntent>): Observable<BandwidthFormIntent> = Observable.merge(
        intents.ofType(BandwidthFormIntent.Init::class.java).take(1),
        intents.filter {
            !BandwidthFormIntent.Init::class.java.isInstance(it)
        }
    )

    private fun commit(): Observable<BandwidthFormRenderAction> {
        return Observable.just(BandwidthFormRenderAction.OnProgress)
    }
}