package com.memtrip.eosreach.app.account.resources.manage.manageram

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ManageRamViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<ManageRamIntent, ManageRamRenderAction, ManageRamViewState>(
    ManageRamViewState(
        view = ManageRamViewState.View.Idle,
        page = ManageRamFragmentPagerAdapter.Page.BUY
    ),
    application
) {

    override fun dispatcher(intent: ManageRamIntent): Observable<ManageRamRenderAction> = when (intent) {
        ManageRamIntent.BuyRamTabIdle -> Observable.just(ManageRamRenderAction.BuyRamTabIdle)
        ManageRamIntent.SellRamTabIdle -> Observable.just(ManageRamRenderAction.SellRamTabIdle)
        is ManageRamIntent.Init -> Observable.just(ManageRamRenderAction.Init(intent.eosAccount))
    }

    override fun reducer(previousState: ManageRamViewState, renderAction: ManageRamRenderAction): ManageRamViewState = when (renderAction) {
        is ManageRamRenderAction.Init -> previousState.copy(
            view = ManageRamViewState.View.Populate(renderAction.eosAccount))
        ManageRamRenderAction.BuyRamTabIdle -> previousState.copy(
            view = ManageRamViewState.View.Idle,
            page = ManageRamFragmentPagerAdapter.Page.BUY)
        ManageRamRenderAction.SellRamTabIdle -> previousState.copy(
            view = ManageRamViewState.View.Idle,
            page = ManageRamFragmentPagerAdapter.Page.SELL)
    }

    override fun filterIntents(intents: Observable<ManageRamIntent>): Observable<ManageRamIntent> = Observable.merge(
        intents.ofType(ManageRamIntent.Init::class.java).take(1),
        intents.filter {
            !ManageRamIntent.Init::class.java.isInstance(it)
        }
    )
}