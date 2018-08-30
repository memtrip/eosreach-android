package com.memtrip.eosreach.app.account.balance

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class BalanceViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<BalanceIntent, BalanceRenderAction, BalanceViewState>(
    BalanceViewState(view = BalanceViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: BalanceIntent): Observable<BalanceRenderAction> = when (intent) {
        is BalanceIntent.Init -> Observable.just(BalanceRenderAction.Populate(intent.accountBalances))
        BalanceIntent.Idle -> Observable.just(BalanceRenderAction.Idle)
        BalanceIntent.NavigateToCreateAccount -> Observable.just(BalanceRenderAction.NavigateToCreateAccount)
    }

    override fun reducer(previousState: BalanceViewState, renderAction: BalanceRenderAction): BalanceViewState = when (renderAction) {
        BalanceRenderAction.Idle -> previousState.copy(BalanceViewState.View.Idle)
        is BalanceRenderAction.Populate -> previousState.copy(BalanceViewState.View.Populate(renderAction.accountBalances))
        BalanceRenderAction.NavigateToCreateAccount -> previousState.copy(BalanceViewState.View.NavigateToCreateAccount)
    }

    override fun filterIntents(intents: Observable<BalanceIntent>): Observable<BalanceIntent> = Observable.merge(
        intents.ofType(BalanceIntent.Init::class.java).take(1),
        intents.filter {
            !BalanceIntent.Init::class.java.isInstance(it)
        }
    )
}