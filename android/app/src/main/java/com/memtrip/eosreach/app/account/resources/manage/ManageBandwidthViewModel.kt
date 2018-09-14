package com.memtrip.eosreach.app.account.resources.manage

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ManageBandwidthViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<ManageBandwidthIntent, ManageBandwidthRenderAction, ManageBandwidthViewState>(
    ManageBandwidthViewState(view = ManageBandwidthViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: ManageBandwidthIntent): Observable<ManageBandwidthRenderAction> = when (intent) {
        is ManageBandwidthIntent.Init -> Observable.just(ManageBandwidthRenderAction.OnProgress)
    }

    override fun reducer(previousState: ManageBandwidthViewState, renderAction: ManageBandwidthRenderAction): ManageBandwidthViewState = when (renderAction) {
        ManageBandwidthRenderAction.OnProgress -> previousState.copy(view = ManageBandwidthViewState.View.OnProgress)
        ManageBandwidthRenderAction.OnError -> previousState.copy(view = ManageBandwidthViewState.View.OnError)
    }

    override fun filterIntents(intents: Observable<ManageBandwidthIntent>): Observable<ManageBandwidthIntent> = Observable.merge(
        intents.ofType(ManageBandwidthIntent.Init::class.java).take(1),
        intents.filter {
            !ManageBandwidthIntent.Init::class.java.isInstance(it)
        }
    )
}