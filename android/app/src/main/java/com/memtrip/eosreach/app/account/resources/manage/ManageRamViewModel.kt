package com.memtrip.eosreach.app.account.resources.manage

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ManageRamViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<ManageRamIntent, ManageRamRenderAction, ManageRamViewState>(
    ManageRamViewState(view = ManageRamViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: ManageRamIntent): Observable<ManageRamRenderAction> = when (intent) {
        is ManageRamIntent.Init -> Observable.just(ManageRamRenderAction.OnProgress)
    }

    override fun reducer(previousState: ManageRamViewState, renderAction: ManageRamRenderAction): ManageRamViewState = when (renderAction) {
        ManageRamRenderAction.OnProgress -> previousState.copy(view = ManageRamViewState.View.OnProgress)
        ManageRamRenderAction.OnError -> previousState.copy(view = ManageRamViewState.View.OnError)
    }

    override fun filterIntents(intents: Observable<ManageRamIntent>): Observable<ManageRamIntent> = Observable.merge(
        intents.ofType(ManageRamIntent.Init::class.java).take(1),
        intents.filter {
            !ManageRamIntent.Init::class.java.isInstance(it)
        }
    )
}