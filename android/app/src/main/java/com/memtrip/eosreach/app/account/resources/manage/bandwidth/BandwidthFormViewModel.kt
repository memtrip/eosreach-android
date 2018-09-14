package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable

abstract class BandwidthFormViewModel(
    application: Application
) : MxViewModel<BandwidthFormIntent, BandwidthFormRenderAction, BandwidthFormViewState>(
    BandwidthFormViewState(view = BandwidthFormViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: BandwidthFormIntent): Observable<BandwidthFormRenderAction> = when (intent) {
        is BandwidthFormIntent.Init -> Observable.just(BandwidthFormRenderAction.Idle)
        BandwidthFormIntent.Idle -> Observable.just(BandwidthFormRenderAction.Idle)
        is BandwidthFormIntent.Confirm -> Observable.just(BandwidthFormRenderAction.NavigateToConfirm(BandwidthBundle(
            intent.fromAccount,
            intent.cpuAmount,
            intent.netAmount,
            intent.bandwidthCommitType
        )))
    }

    override fun reducer(previousState: BandwidthFormViewState, renderAction: BandwidthFormRenderAction): BandwidthFormViewState = when (renderAction) {
        BandwidthFormRenderAction.Idle -> previousState.copy(
            view = BandwidthFormViewState.View.Idle)
        is BandwidthFormRenderAction.NavigateToConfirm -> previousState.copy(
            view = BandwidthFormViewState.View.NavigateToConfirm(renderAction.bandwidthBundle))
    }

    override fun filterIntents(intents: Observable<BandwidthFormIntent>): Observable<BandwidthFormIntent> = Observable.merge(
        intents.ofType(BandwidthFormIntent.Init::class.java).take(1),
        intents.filter {
            !BandwidthFormIntent.Init::class.java.isInstance(it)
        }
    )
}