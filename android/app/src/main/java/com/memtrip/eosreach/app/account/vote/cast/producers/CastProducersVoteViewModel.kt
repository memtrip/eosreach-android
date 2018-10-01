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
package com.memtrip.eosreach.app.account.vote.cast.producers

import android.app.Application
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccountVote
import com.memtrip.eosreach.api.vote.VoteRequest
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class CastProducersVoteViewModel @Inject internal constructor(
    private val voteRequest: VoteRequest,
    application: Application
) : MxViewModel<CastProducersVoteIntent, CastProducersVoteRenderAction, CastProducersVoteViewState>(
    CastProducersVoteViewState(view = CastProducersVoteViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: CastProducersVoteIntent): Observable<CastProducersVoteRenderAction> = when (intent) {
        CastProducersVoteIntent.Idle -> Observable.just(CastProducersVoteRenderAction.Idle)
        is CastProducersVoteIntent.Init -> Observable.just(populate(intent.eosAccountVote))
        is CastProducersVoteIntent.AddProducerField -> Observable.just(addProducerField(intent.nextPosition, intent.currentTotal))
        is CastProducersVoteIntent.RemoveProducerField -> Observable.just(removeProducerField(intent.removePosition))
        is CastProducersVoteIntent.Vote -> vote(intent.voterAccountName, intent.blockProducers)
        is CastProducersVoteIntent.ViewLog -> Observable.just(CastProducersVoteRenderAction.ViewLog(intent.log))
    }

    override fun reducer(previousState: CastProducersVoteViewState, renderAction: CastProducersVoteRenderAction): CastProducersVoteViewState = when (renderAction) {
        CastProducersVoteRenderAction.Idle -> previousState.copy(
            view = CastProducersVoteViewState.View.Idle)
        is CastProducersVoteRenderAction.AddProducerField -> previousState.copy(
            view = CastProducersVoteViewState.View.AddProducerField(renderAction.nextPosition))
        is CastProducersVoteRenderAction.AddExistingProducers -> previousState.copy(
            view = CastProducersVoteViewState.View.AddExistingProducers(renderAction.producers))
        is CastProducersVoteRenderAction.RemoveProducerField -> previousState.copy(
            view = CastProducersVoteViewState.View.RemoveProducerField(renderAction.position))
        CastProducersVoteRenderAction.OnProgress -> previousState.copy(
            view = CastProducersVoteViewState.View.OnProgress)
        is CastProducersVoteRenderAction.OnError -> previousState.copy(
            view = CastProducersVoteViewState.View.OnError(renderAction.message, renderAction.log))
        is CastProducersVoteRenderAction.OnSuccess -> previousState.copy(
            view = CastProducersVoteViewState.View.OnSuccess)
        is CastProducersVoteRenderAction.ViewLog -> previousState.copy(
            view = CastProducersVoteViewState.View.ViewLog(renderAction.log))
    }

    override fun filterIntents(intents: Observable<CastProducersVoteIntent>): Observable<CastProducersVoteIntent> = Observable.merge(
        intents.ofType(CastProducersVoteIntent.Init::class.java).take(1),
        intents.filter {
            !CastProducersVoteIntent.Init::class.java.isInstance(it)
        }
    )

    private fun populate(eosAccountVote: EosAccountVote?): CastProducersVoteRenderAction {
        if (eosAccountVote != null && eosAccountVote.producers.isNotEmpty()) {
            return CastProducersVoteRenderAction.AddExistingProducers(eosAccountVote.producers)
        } else {
            return CastProducersVoteRenderAction.AddProducerField(0)
        }
    }

    private fun vote(
        voterAccountName: String,
        blockProducers: List<String>
    ): Observable<CastProducersVoteRenderAction> {
        return voteRequest.voteForProducer(
            voterAccountName,
            blockProducers
        ).flatMap { result ->
            if (result.success) {
                Single.just(CastProducersVoteRenderAction.OnSuccess)
            } else {
                Single.just(CastProducersVoteRenderAction.OnError(
                    context().getString(R.string.vote_cast_vote_error_message),
                    result.apiError!!.body))
            }
        }.toObservable().startWith(CastProducersVoteRenderAction.OnProgress)
    }

    private fun addProducerField(position: Int, total: Int): CastProducersVoteRenderAction {
        return if (total >= 29) {
            CastProducersVoteRenderAction.Idle
        } else {
            CastProducersVoteRenderAction.AddProducerField(position)
        }
    }

    private fun removeProducerField(position: Int): CastProducersVoteRenderAction {
        return if (position == 0) {
            CastProducersVoteRenderAction.Idle
        } else {
            CastProducersVoteRenderAction.RemoveProducerField(position)
        }
    }
}