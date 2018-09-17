package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class BandwidthManageViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<BandwidthManageIntent, BandwidthManageRenderAction, BandwidthManageViewState>(
    BandwidthManageViewState(
        view = BandwidthManageViewState.View.Idle,
        page = BandwidthManageFragmentPagerAdapter.Page.DELEGATE
    ),
    application
) {

    override fun dispatcher(intent: BandwidthManageIntent): Observable<BandwidthManageRenderAction> = when (intent) {
        is BandwidthManageIntent.Init -> Observable.just(BandwidthManageRenderAction.Init)
        BandwidthManageIntent.DelegateBandwidthTabIdle -> Observable.just(BandwidthManageRenderAction.DelegateBandwidthTabIdle)
        BandwidthManageIntent.UnDelegateBandwidthTabIdle -> Observable.just(BandwidthManageRenderAction.UnDelegateBandwidthTabIdle)
    }

    override fun reducer(previousState: BandwidthManageViewState, renderAction: BandwidthManageRenderAction): BandwidthManageViewState = when (renderAction) {
        is BandwidthManageRenderAction.Init -> previousState.copy(
            view = BandwidthManageViewState.View.Populate)
        BandwidthManageRenderAction.DelegateBandwidthTabIdle -> previousState.copy(
            view = BandwidthManageViewState.View.Idle,
            page = BandwidthManageFragmentPagerAdapter.Page.DELEGATE)
        BandwidthManageRenderAction.UnDelegateBandwidthTabIdle -> previousState.copy(
            view = BandwidthManageViewState.View.Idle,
            page = BandwidthManageFragmentPagerAdapter.Page.UNDELEGATE)
    }

    override fun filterIntents(intents: Observable<BandwidthManageIntent>): Observable<BandwidthManageIntent> = Observable.merge(
        intents.ofType(BandwidthManageIntent.Init::class.java).take(1),
        intents.filter {
            !BandwidthManageIntent.Init::class.java.isInstance(it)
        }
    )
}