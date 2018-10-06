/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.memtrip.eosreach.app.account.vote.cast.proxy

import android.app.Application
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.vote.VoteRequest
import com.memtrip.eosreach.db.transaction.InsertTransactionLog
import com.memtrip.eosreach.db.transaction.TransactionLogEntity
import com.memtrip.eosreach.utils.fullDateTime
import com.memtrip.eosreach.utils.toLocalDateTime
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single
import java.util.Date
import javax.inject.Inject

class CastProxyVoteViewModel @Inject internal constructor(
    private val voteRequest: VoteRequest,
    private val insertTransactionLog: InsertTransactionLog,
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
            view = CastProxyVoteViewState.View.OnError(renderAction.message, renderAction.log),
            proxyVote = renderAction.proxyVote)
        CastProxyVoteRenderAction.OnSuccess -> previousState.copy(
            view = CastProxyVoteViewState.View.OnSuccess)
        is CastProxyVoteRenderAction.ViewLog -> previousState.copy(
            view = CastProxyVoteViewState.View.ViewLog(renderAction.log))
    }

    private fun giveProxyVote(voterAccountName: String, proxyVoteAccountName: String): Observable<CastProxyVoteRenderAction> {
        return voteRequest.voteForProxy(
            voterAccountName,
            proxyVoteAccountName
        ).flatMap<CastProxyVoteRenderAction> { result ->
            if (result.success) {
                val transaction = result.data!!
                insertTransactionLog.insert(TransactionLogEntity(
                    transactionId = transaction.transactionId,
                    formattedDate = Date().toLocalDateTime().fullDateTime())).toSingleDefault<CastProxyVoteRenderAction>(
                    CastProxyVoteRenderAction.OnSuccess
                )
            } else {
                Single.just(CastProxyVoteRenderAction.OnError(
                    context().getString(R.string.vote_cast_proxy_vote_error),
                    result.apiError!!.body,
                    proxyVoteAccountName))
            }
        }.toObservable().startWith(CastProxyVoteRenderAction.OnProgress)
    }
}