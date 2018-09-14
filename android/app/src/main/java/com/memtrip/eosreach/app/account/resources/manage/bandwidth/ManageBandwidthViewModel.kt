package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ManageBandwidthViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<ManageBandwidthIntent, ManageBandwidthRenderAction, ManageBandwidthViewState>(
    ManageBandwidthViewState(
        view = ManageBandwidthViewState.View.Idle,
        page = ManageBandwidthFragmentPagerAdapter.Page.DELEGATE
    ),
    application
) {

    override fun dispatcher(intent: ManageBandwidthIntent): Observable<ManageBandwidthRenderAction> = when (intent) {
        is ManageBandwidthIntent.Init -> Observable.just(ManageBandwidthRenderAction.Init(intent.eosAccount))
        ManageBandwidthIntent.DelegateBandwidthTabIdle -> Observable.just(ManageBandwidthRenderAction.DelegateBandwidthTabIdle)
        ManageBandwidthIntent.UnDelegateBandwidthTabIdle -> Observable.just(ManageBandwidthRenderAction.UnDelegateBandwidthTabIdle)
    }

    override fun reducer(previousState: ManageBandwidthViewState, renderAction: ManageBandwidthRenderAction): ManageBandwidthViewState = when (renderAction) {
        is ManageBandwidthRenderAction.Init -> previousState.copy(
            view = ManageBandwidthViewState.View.Populate(renderAction.eosAccount))
        ManageBandwidthRenderAction.DelegateBandwidthTabIdle -> previousState.copy(
            view = ManageBandwidthViewState.View.Idle,
            page = ManageBandwidthFragmentPagerAdapter.Page.DELEGATE)
        ManageBandwidthRenderAction.UnDelegateBandwidthTabIdle -> previousState.copy(
            view = ManageBandwidthViewState.View.Idle,
            page = ManageBandwidthFragmentPagerAdapter.Page.UNDELEGATE)
    }

    override fun filterIntents(intents: Observable<ManageBandwidthIntent>): Observable<ManageBandwidthIntent> = Observable.merge(
        intents.ofType(ManageBandwidthIntent.Init::class.java).take(1),
        intents.filter {
            !ManageBandwidthIntent.Init::class.java.isInstance(it)
        }
    )
}