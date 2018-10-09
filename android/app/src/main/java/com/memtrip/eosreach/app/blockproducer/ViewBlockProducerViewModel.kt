package com.memtrip.eosreach.app.blockproducer

import android.app.Application
import android.net.Uri
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ViewBlockProducerViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<ViewBlockProducerIntent, ViewBlockProducerRenderAction, ViewBlockProducerViewState>(
    ViewBlockProducerViewState(view = ViewBlockProducerViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: ViewBlockProducerIntent): Observable<ViewBlockProducerRenderAction> = when (intent) {
        is ViewBlockProducerIntent.InitWithDetails -> Observable.just(ViewBlockProducerRenderAction.Populate(intent.blockProducerDetails))
        is ViewBlockProducerIntent.InitWithName -> TODO()
        ViewBlockProducerIntent.Idle -> Observable.just(ViewBlockProducerRenderAction.Idle)
        is ViewBlockProducerIntent.NavigateToUrl -> Observable.just(validateUrl(intent.url))
    }

    override fun reducer(previousState: ViewBlockProducerViewState, renderAction: ViewBlockProducerRenderAction): ViewBlockProducerViewState = when (renderAction) {
        ViewBlockProducerRenderAction.Idle -> previousState.copy(view = ViewBlockProducerViewState.View.Idle)
        ViewBlockProducerRenderAction.OnProgress -> previousState.copy(view = ViewBlockProducerViewState.View.OnProgress)
        ViewBlockProducerRenderAction.OnError -> previousState.copy(view = ViewBlockProducerViewState.View.OnError)
        is ViewBlockProducerRenderAction.OnInvalidUrl -> previousState.copy(view = ViewBlockProducerViewState.View.OnInvalidUrl(renderAction.url))
        is ViewBlockProducerRenderAction.NavigateToUrl -> previousState.copy(view = ViewBlockProducerViewState.View.NavigateToUrl(renderAction.url))
        is ViewBlockProducerRenderAction.Populate -> previousState.copy(view = ViewBlockProducerViewState.View.Populate(renderAction.blockProducerDetails))
    }

    override fun filterIntents(intents: Observable<ViewBlockProducerIntent>): Observable<ViewBlockProducerIntent> = Observable.merge(
        intents.ofType(ViewBlockProducerIntent.InitWithDetails::class.java).take(1),
        intents.filter {
            !ViewBlockProducerIntent.InitWithDetails::class.java.isInstance(it)
        }
    )

    private fun validateUrl(url: String): ViewBlockProducerRenderAction {
        return if (checkParseUrl(url)) {
            ViewBlockProducerRenderAction.NavigateToUrl(url)
        } else {
            ViewBlockProducerRenderAction.OnInvalidUrl(url)
        }
    }

    private fun checkParseUrl(url: String): Boolean {
        return try {
            Uri.parse(url)
            true
        } catch (e: Exception) {
            false
        }
    }
}