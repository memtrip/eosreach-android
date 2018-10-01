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

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class CastProxyVoteRenderAction : MxRenderAction {
    object Idle : CastProxyVoteRenderAction()
    object OnProgress : CastProxyVoteRenderAction()
    data class OnError(
        val message: String,
        val log: String,
        val proxyVote: String
    ) : CastProxyVoteRenderAction()
    object OnSuccess : CastProxyVoteRenderAction()
    data class ViewLog(val log: String) : CastProxyVoteRenderAction()
}

interface CastProxyVoteViewLayout : MxViewLayout {
    fun showProgress()
    fun showError(message: String, log: String)
    fun onSuccess()
    fun viewLog(log: String)
    fun populateProxyVoteInput(value: String)
}

class CastProxyVoteViewRenderer @Inject internal constructor() : MxViewRenderer<CastProxyVoteViewLayout, CastProxyVoteViewState> {
    override fun layout(layout: CastProxyVoteViewLayout, state: CastProxyVoteViewState) {
        state.proxyVote?.let {
            layout.populateProxyVoteInput(state.proxyVote)
        }

        doLayout(layout, state)
    }

    private fun doLayout(layout: CastProxyVoteViewLayout, state: CastProxyVoteViewState): Unit = when (state.view) {
        CastProxyVoteViewState.View.Idle -> {
        }
        CastProxyVoteViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is CastProxyVoteViewState.View.OnError -> {
            layout.showError(state.view.message, state.view.log)
        }
        CastProxyVoteViewState.View.OnSuccess -> {
            layout.onSuccess()
        }
        is CastProxyVoteViewState.View.ViewLog -> {
            layout.viewLog(state.view.log)
        }
    }
}