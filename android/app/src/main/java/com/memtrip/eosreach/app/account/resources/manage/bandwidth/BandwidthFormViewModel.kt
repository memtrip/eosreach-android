package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.app.Application
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.price.BalanceFormatter
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable

abstract class BandwidthFormViewModel(
    application: Application
) : MxViewModel<BandwidthFormIntent, BandwidthFormRenderAction, BandwidthFormViewState>(
    BandwidthFormViewState(view = BandwidthFormViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: BandwidthFormIntent): Observable<BandwidthFormRenderAction> = when (intent) {
        is BandwidthFormIntent.Init -> Observable.just(BandwidthFormRenderAction.Populate(intent.contractAccountBalance))
        BandwidthFormIntent.Idle -> Observable.just(BandwidthFormRenderAction.Idle)
        is BandwidthFormIntent.Confirm -> Observable.just(BandwidthFormRenderAction.NavigateToConfirm(
            intent.bandwidthCommitType,
            intent.fromAccount,
            intent.cpuAmount,
            intent.netAmount,
            intent.contractAccountBalance
        ))
    }

    override fun reducer(previousState: BandwidthFormViewState, renderAction: BandwidthFormRenderAction): BandwidthFormViewState = when (renderAction) {
        BandwidthFormRenderAction.Idle -> previousState.copy(
            view = BandwidthFormViewState.View.Idle)
        is BandwidthFormRenderAction.Populate -> previousState.copy(
            view = BandwidthFormViewState.View.Populate(renderAction.contractAccountBalance))
        is BandwidthFormRenderAction.NavigateToConfirm -> previousState.copy(
            view = BandwidthFormViewState.View.NavigateToConfirm(createBandwidthBundle(
                renderAction.bandwidthCommitType,
                renderAction.fromAccount,
                renderAction.cpuAmount,
                renderAction.netAmount,
                renderAction.contractAccountBalance)))
    }

    override fun filterIntents(intents: Observable<BandwidthFormIntent>): Observable<BandwidthFormIntent> = Observable.merge(
        intents.ofType(BandwidthFormIntent.Init::class.java).take(1),
        intents.filter {
            !BandwidthFormIntent.Init::class.java.isInstance(it)
        }
    )

    private fun createBandwidthBundle(
        bandwidthCommitType: BandwidthCommitType,
        netAmount: String,
        cpuAmount: String,
        fromAccount: String,
        contractAccountBalance: ContractAccountBalance
    ): BandwidthBundle {
        return BandwidthBundle(
            bandwidthCommitType,
            BalanceFormatter.create(netAmount, contractAccountBalance.balance.symbol),
            BalanceFormatter.create(cpuAmount, contractAccountBalance.balance.symbol),
            fromAccount
        )
    }
}