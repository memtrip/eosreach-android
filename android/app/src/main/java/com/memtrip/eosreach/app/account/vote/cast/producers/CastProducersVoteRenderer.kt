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

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class CastProducersVoteRenderAction : MxRenderAction {
    object Idle : CastProducersVoteRenderAction()
    object OnProgress : CastProducersVoteRenderAction()
    data class AddExistingProducers(val producers: List<String>) : CastProducersVoteRenderAction()
    data class AddProducerField(val nextPosition: Int) : CastProducersVoteRenderAction()
    data class RemoveProducerField(val position: Int) : CastProducersVoteRenderAction()
    data class OnError(val message: String, val log: String) : CastProducersVoteRenderAction()
    object OnSuccess : CastProducersVoteRenderAction()
    data class ViewLog(val log: String) : CastProducersVoteRenderAction()
}

interface CastProducersVoteViewLayout : MxViewLayout {
    fun populateWidthExistingProducerList(producers: List<String>)
    fun adProducerRow(position: Int)
    fun removeProducerField(position: Int)
    fun showProgress()
    fun showError(message: String, log: String)
    fun onSuccess()
    fun viewLog(log: String)
}

class CastProducersVoteViewRenderer @Inject internal constructor() : MxViewRenderer<CastProducersVoteViewLayout, CastProducersVoteViewState> {
    override fun layout(layout: CastProducersVoteViewLayout, state: CastProducersVoteViewState): Unit = when (state.view) {
        CastProducersVoteViewState.View.Idle -> {
        }
        is CastProducersVoteViewState.View.AddProducerField -> {
            layout.adProducerRow(state.view.nextPosition)
        }
        is CastProducersVoteViewState.View.AddExistingProducers -> {
            layout.populateWidthExistingProducerList(state.view.producers)
        }
        is CastProducersVoteViewState.View.RemoveProducerField -> {
            layout.removeProducerField(state.view.position)
        }
        CastProducersVoteViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is CastProducersVoteViewState.View.OnError -> {
            layout.showError(state.view.message, state.view.log)
        }
        is CastProducersVoteViewState.View.OnSuccess -> {
            layout.onSuccess()
        }
        is CastProducersVoteViewState.View.ViewLog -> {
            layout.viewLog(state.view.log)
        }
    }
}