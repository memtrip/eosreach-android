package com.memtrip.eosreach.app.price.currencypairing

import android.app.Application
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.price.EosPriceUseCase
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class CurrencyPairingViewModel @Inject internal constructor(
    private val eosPriceUseCase: EosPriceUseCase,
    application: Application
) : MxViewModel<CurrencyPairingIntent, CurrencyPairingRenderAction, CurrencyPairingViewState>(
    CurrencyPairingViewState(view = CurrencyPairingViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: CurrencyPairingIntent): Observable<CurrencyPairingRenderAction> = when (intent) {
        is CurrencyPairingIntent.Init -> Observable.just(CurrencyPairingRenderAction.Idle)
        is CurrencyPairingIntent.CurrencyPair -> checkCurrencyPair(intent.currencyPair)
    }

    override fun reducer(previousState: CurrencyPairingViewState, renderAction: CurrencyPairingRenderAction): CurrencyPairingViewState = when (renderAction) {
        CurrencyPairingRenderAction.Idle -> previousState.copy(view = CurrencyPairingViewState.View.Idle)
        CurrencyPairingRenderAction.OnProgress -> previousState.copy(view = CurrencyPairingViewState.View.OnProgress)
        is CurrencyPairingRenderAction.OnError -> previousState.copy(view = CurrencyPairingViewState.View.OnError(renderAction.message))
        CurrencyPairingRenderAction.OnSuccess -> previousState.copy(view = CurrencyPairingViewState.View.OnSuccess)
    }

    override fun filterIntents(intents: Observable<CurrencyPairingIntent>): Observable<CurrencyPairingIntent> = Observable.merge(
        intents.ofType(CurrencyPairingIntent.Init::class.java).take(1),
        intents.filter {
            !CurrencyPairingIntent.Init::class.java.isInstance(it)
        }
    )

    private fun checkCurrencyPair(currencyCode: String): Observable<CurrencyPairingRenderAction> {
        if (currencyCode.isEmpty() || currencyCode.length < 3) {
            return Observable.just(CurrencyPairingRenderAction.OnError(
                context().getString(R.string.currency_pairing_failed_too_short)))
        } else {
            return eosPriceUseCase.refreshPrice(currencyCode).map { eosPrice ->
                if (eosPrice.unavailable) {
                    CurrencyPairingRenderAction.OnError(
                        context().getString(R.string.currency_pairing_failed, currencyCode))
                } else {
                    CurrencyPairingRenderAction.OnSuccess
                }
            }.toObservable().startWith(CurrencyPairingRenderAction.OnProgress)
        }
    }
}