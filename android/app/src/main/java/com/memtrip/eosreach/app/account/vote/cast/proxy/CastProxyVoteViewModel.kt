package com.memtrip.eosreach.app.account.vote.cast.proxy

import android.app.Application
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.vote.VoteRequest
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class CastProxyVoteViewModel @Inject internal constructor(
    private val voteRequest: VoteRequest,
    application: Application
) : MxViewModel<CastProxyVoteIntent, CastProxyVoteRenderAction, CastProxyVoteViewState>(
    CastProxyVoteViewState(view = CastProxyVoteViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: CastProxyVoteIntent): Observable<CastProxyVoteRenderAction> = when (intent) {
        CastProxyVoteIntent.Idle -> Observable.just(CastProxyVoteRenderAction.Idle)
        is CastProxyVoteIntent.Vote -> giveProxyVote(intent.voterAccountName, intent.proxyAccountName)
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

    private fun giveProxyVote(voterAccountName: String, proxyVoteAccountName: String): Observable<CastProxyVoteRenderAction> {
        return voteRequest.voteForProxy(
            voterAccountName,
            proxyVoteAccountName
        ).map<CastProxyVoteRenderAction> { result ->
            if (result.success) {
                CastProxyVoteRenderAction.OnSuccess
            } else {
                CastProxyVoteRenderAction.OnError(
                    context().getString(R.string.cast_proxy_vote_error),
                    result.apiError!!.body)
            }
        }.toObservable().startWith(CastProxyVoteRenderAction.OnProgress)
    }
}