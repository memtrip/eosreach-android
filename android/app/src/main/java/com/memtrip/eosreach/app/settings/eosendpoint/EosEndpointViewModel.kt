package com.memtrip.eosreach.app.settings.eosendpoint

import android.app.Application
import com.memtrip.eosreach.R
import com.memtrip.eosreach.db.blockproducer.DeleteBlockProducers
import com.memtrip.eosreach.db.sharedpreferences.EosEndpoint
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class EosEndpointViewModel @Inject internal constructor(
    private val eosEndpointUseCase: EosEndpointUseCase,
    private val eosEndPoint: EosEndpoint,
    private val deleteBlockProducers: DeleteBlockProducers,
    application: Application
) : MxViewModel<EosEndpointIntent, EosEndpointRenderAction, EosEndpointViewState>(
    EosEndpointViewState(
        endpointUrl = eosEndPoint.get(),
        view = EosEndpointViewState.View.Idle
    ),
    application
) {

    override fun dispatcher(intent: EosEndpointIntent): Observable<EosEndpointRenderAction> = when (intent) {
        EosEndpointIntent.Idle -> Observable.just(EosEndpointRenderAction.Idle)
        is EosEndpointIntent.Init -> Observable.just(EosEndpointRenderAction.Idle)
        is EosEndpointIntent.ChangeEndpoint -> changeEndpoint(intent.endpoint)
        EosEndpointIntent.NavigateToBlockProducerList -> Observable.just(EosEndpointRenderAction.NavigateToBlockProducerList)
    }

    override fun reducer(previousState: EosEndpointViewState, renderAction: EosEndpointRenderAction): EosEndpointViewState = when (renderAction) {
        EosEndpointRenderAction.Idle -> previousState.copy(
            view = EosEndpointViewState.View.Idle)
        EosEndpointRenderAction.OnProgress -> previousState.copy(
            view = EosEndpointViewState.View.OnProgress)
        is EosEndpointRenderAction.OnError -> previousState.copy(
            view = EosEndpointViewState.View.OnError(renderAction.message))
        EosEndpointRenderAction.OnSuccess -> previousState.copy(
            view = EosEndpointViewState.View.OnSuccess)
        EosEndpointRenderAction.NavigateToBlockProducerList -> previousState.copy(
            view = EosEndpointViewState.View.NavigateToBlockProducerList)
    }

    override fun filterIntents(intents: Observable<EosEndpointIntent>): Observable<EosEndpointIntent> = Observable.merge(
        intents.ofType(EosEndpointIntent.Init::class.java).take(1),
        intents.filter {
            !EosEndpointIntent.Init::class.java.isInstance(it)
        }
    )

    private fun changeEndpoint(endpointUrl: String): Observable<EosEndpointRenderAction> {
        if (!endpointUrl.startsWith("http://") && !endpointUrl.startsWith("https://")) {
            return Observable.just(EosEndpointRenderAction.OnError(
                context().getString(R.string.eos_endpoint_validation_invalid_url)))
        } else if (endpointUrl == eosEndPoint.get()) {
            return Observable.just(EosEndpointRenderAction.OnError(
                context().getString(R.string.eos_endpoint_validation_nothing_changed, endpointUrl)))
        } else {
            return eosEndpointUseCase.getInfo(endpointUrl)
                .flatMap { result ->
                    if (result.success) {
                        if (!endpointUrl.endsWith("/")) {
                            eosEndPoint.put("$endpointUrl/")
                        } else {
                            eosEndPoint.put(endpointUrl)
                        }
                        deleteBlockProducers
                            .remove()
                            .toSingleDefault<EosEndpointRenderAction.OnSuccess>(EosEndpointRenderAction.OnSuccess)
                    } else {
                        Single.just(EosEndpointRenderAction.OnError(
                            context().getString(R.string.eos_endpoint_validation_generic)))
                    }
                }.toObservable().startWith(EosEndpointRenderAction.OnProgress)
        }
    }
}