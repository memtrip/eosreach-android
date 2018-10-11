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
package com.memtrip.eosreach.app.proxyvoter

import com.memtrip.eosreach.api.proxyvoter.ProxyVoterDetails
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ProxyVoterListRenderAction : MxRenderAction {
    object Idle : ProxyVoterListRenderAction()
    object OnProgress : ProxyVoterListRenderAction()
    object OnError : ProxyVoterListRenderAction()
    data class OnSuccess(val proxyVoterDetails: List<ProxyVoterDetails>) : ProxyVoterListRenderAction()
    object OnMoreError : ProxyVoterListRenderAction()
    object OnMoreProgress : ProxyVoterListRenderAction()
    data class OnMoreSuccess(val proxyVoterDetails: List<ProxyVoterDetails>) : ProxyVoterListRenderAction()
    data class ProxyInformationSelected(val proxyVoterDetails: ProxyVoterDetails) : ProxyVoterListRenderAction()
    data class ProxyVoterSelected(val proxyVoterDetails: ProxyVoterDetails) : ProxyVoterListRenderAction()
}

interface ProxyVoterListViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
    fun populate(voteProxies: List<ProxyVoterDetails>)
    fun loadMoreProgress()
    fun loadMoreError()
    fun selectProxyVoter(proxyVoterDetails: ProxyVoterDetails)
    fun navigateToProxyVoterDetails(proxyVoterDetails: ProxyVoterDetails)
}

class ProxyVoterListViewRenderer @Inject internal constructor() : MxViewRenderer<ProxyVoterListViewLayout, ProxyVoterListViewState> {
    override fun layout(layout: ProxyVoterListViewLayout, state: ProxyVoterListViewState): Unit = when (state.view) {
        ProxyVoterListViewState.View.Idle -> {
        }
        ProxyVoterListViewState.View.OnProgress -> {
            layout.showProgress()
        }
        ProxyVoterListViewState.View.OnError -> {
            layout.showError()
        }
        is ProxyVoterListViewState.View.OnSuccess -> {
            layout.populate(state.view.proxyVoterDetails)
        }
        is ProxyVoterListViewState.View.OnMoreSuccess -> {
            layout.populate(state.view.proxyVoterDetails)
        }
        ProxyVoterListViewState.View.OnMoreError -> {
            layout.loadMoreError()
        }
        ProxyVoterListViewState.View.OnMoreProgress -> {
            layout.loadMoreProgress()
        }
        is ProxyVoterListViewState.View.ProxyVoterSelected -> {
            layout.selectProxyVoter(state.view.proxyVoterDetails)
        }
        is ProxyVoterListViewState.View.ProxyVoterInformationSelected -> {
            layout.navigateToProxyVoterDetails(state.view.proxyVoterDetails)
        }
    }
}