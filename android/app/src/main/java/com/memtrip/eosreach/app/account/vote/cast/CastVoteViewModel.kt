package com.memtrip.eosreach.app.account.vote.cast

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class CastVoteViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<CastVoteIntent, CastVoteRenderAction, CastVoteViewState>(
    CastVoteViewState(view = CastVoteViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: CastVoteIntent): Observable<CastVoteRenderAction> = when (intent) {
        is CastVoteIntent.Init -> Observable.just(CastVoteRenderAction.Populate(intent.eosAccount, intent.page))
        CastVoteIntent.CastProducerVoteTabIdle -> startingTab(CastVoteFragmentPagerFragment.Page.PRODUCER)
        CastVoteIntent.CastProxyVoteTabIdle -> startingTab(CastVoteFragmentPagerFragment.Page.PROXY)
    }

    override fun reducer(previousState: CastVoteViewState, renderAction: CastVoteRenderAction): CastVoteViewState = when (renderAction) {
        is CastVoteRenderAction.TabIdle -> previousState.copy(
            view = CastVoteViewState.View.Idle,
            page = renderAction.page)
        is CastVoteRenderAction.Populate -> previousState.copy(
            view = CastVoteViewState.View.Populate(renderAction.eosAccount),
            page = renderAction.page)
    }

    override fun filterIntents(intents: Observable<CastVoteIntent>): Observable<CastVoteIntent> = Observable.merge(
        intents.ofType(CastVoteIntent.Init::class.java).take(1),
        intents.filter {
            !CastVoteIntent.Init::class.java.isInstance(it)
        }
    )

    private fun startingTab(page: CastVoteFragmentPagerFragment.Page): Observable<CastVoteRenderAction> = when (page) {
        CastVoteFragmentPagerFragment.Page.PRODUCER -> Observable.just(CastVoteRenderAction.TabIdle(CastVoteFragmentPagerFragment.Page.PRODUCER))
        CastVoteFragmentPagerFragment.Page.PROXY -> Observable.just(CastVoteRenderAction.TabIdle(CastVoteFragmentPagerFragment.Page.PROXY))
    }
}