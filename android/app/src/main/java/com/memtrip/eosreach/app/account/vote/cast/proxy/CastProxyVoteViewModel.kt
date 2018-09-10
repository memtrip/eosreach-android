package com.memtrip.eosreach.app.account.vote.cast.proxy

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class CastProxyVoteViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<CastProxyVoteIntent, CastProxyVoteRenderAction, CastProxyVoteViewState>(
    CastProxyVoteViewState(view = CastProxyVoteViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: CastProxyVoteIntent): Observable<CastProxyVoteRenderAction> = when (intent) {
        CastProxyVoteIntent.Idle -> Observable.just(CastProxyVoteRenderAction.Idle)
        is CastProxyVoteIntent.Vote -> Observable.just(giveProxyVote())
        is CastProxyVoteIntent.ViewLog -> Observable.just(CastProxyVoteRenderAction.ViewLog(intent.log))
    }

    override fun reducer(previousState: CastProxyVoteViewState, renderAction: CastProxyVoteRenderAction): CastProxyVoteViewState = when (renderAction) {
        CastProxyVoteRenderAction.Idle -> previousState.copy(
            view = CastProxyVoteViewState.View.Idle)
        CastProxyVoteRenderAction.OnProgress -> previousState.copy(
            view = CastProxyVoteViewState.View.OnProgress)
        is CastProxyVoteRenderAction.OnError -> previousState.copy(
            view = CastProxyVoteViewState.View.OnError(renderAction.message, renderAction.log))
        CastProxyVoteRenderAction.OnSuccess -> previousState.copy(
            view = CastProxyVoteViewState.View.OnSuccess)
        is CastProxyVoteRenderAction.ViewLog -> previousState.copy(
            view = CastProxyVoteViewState.View.ViewLog(renderAction.log))
    }

    private fun giveProxyVote(): CastProxyVoteRenderAction {
        return CastProxyVoteRenderAction.OnSuccess
    }
}