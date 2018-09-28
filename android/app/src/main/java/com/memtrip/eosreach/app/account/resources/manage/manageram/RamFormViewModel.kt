package com.memtrip.eosreach.app.account.resources.manage.manageram

import android.app.Application
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.app.price.BalanceFormatter
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
        is RamFormIntent.Commit -> Observable.just(navigateToConfirmRamForm(
            intent.kilobytes, intent.ramCommitType))
        is RamFormIntent.ConvertKiloBytesToEOSCost -> Observable.just(calculateEOSCost(
            intent.kb, intent.costPerKb))
    }

    override fun reducer(previousState: RamFormViewState, renderAction: RamFormRenderAction): RamFormViewState = when (renderAction) {
        RamFormRenderAction.Idle -> previousState.copy(
            view = RamFormViewState.View.Idle)
        is RamFormRenderAction.UpdateCostPerKiloByte -> previousState.copy(
            view = RamFormViewState.View.UpdateCostPerKiloByte(renderAction.eosCost))
        is RamFormRenderAction.NavigateToConfirmRamForm -> previousState.copy(
            view = RamFormViewState.View.NavigateToConfirmRamForm(renderAction.kilobytes, renderAction.ramCommitType))
        RamFormRenderAction.EmptyRamError -> previousState.copy(
            view = RamFormViewState.View.EmptyRamError)
    }

    override fun filterIntents(intents: Observable<RamFormIntent>): Observable<RamFormIntent> = Observable.merge(
        intents.ofType(RamFormIntent.Init::class.java).take(1),
        intents.filter {
            !RamFormIntent.Init::class.java.isInstance(it)
        }
    )

    private fun navigateToConfirmRamForm(kb: String, ramCommitType: RamCommitType): RamFormRenderAction {
        val kbValue: Double = if (kb.isEmpty() || kb == ".") { 0.0 } else {
            kb.toDouble()
        }

        return if (kbValue == 0.0) {
            RamFormRenderAction.EmptyRamError
        } else {
            RamFormRenderAction.NavigateToConfirmRamForm(kb, ramCommitType)
        }
    }

    private fun calculateEOSCost(kb: String, costPerKb: Balance): RamFormRenderAction {
        val kbValue: Double = if (kb.isEmpty() || kb == ".") { 0.0 } else {
            kb.toDouble()
        }
        val eosCost =  kbValue * costPerKb.amount
        return RamFormRenderAction.UpdateCostPerKiloByte(
            BalanceFormatter.formatEosBalance(eosCost, costPerKb.symbol))
    }
}