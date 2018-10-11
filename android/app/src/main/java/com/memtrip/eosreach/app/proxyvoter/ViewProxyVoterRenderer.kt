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

import com.memtrip.eosreach.api.blockproducer.BlockProducerDetails
import com.memtrip.eosreach.api.proxyvoter.ProxyVoterDetails
import com.memtrip.eosreach.app.blockproducer.ViewBlockProducerRenderAction
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ViewProxyVoterRenderAction : MxRenderAction {
    object Idle : ViewProxyVoterRenderAction()
    object OnProgress : ViewProxyVoterRenderAction()
    object OnError : ViewProxyVoterRenderAction()
    data class Populate(val proxyVoterDetails: ProxyVoterDetails) : ViewProxyVoterRenderAction()
    data class OnInvalidUrl(val url: String) : ViewProxyVoterRenderAction()
    data class NavigateToUrl(val url: String) : ViewProxyVoterRenderAction()
}

interface ViewProxyVoterViewLayout : MxViewLayout {
    fun showProgress()
    fun showError()
    fun populate(proxyVoterDetails: ProxyVoterDetails)
    fun invalidUrl(url: String)
    fun navigateToUrl(url: String)
}

class ViewProxyVoterViewRenderer @Inject internal constructor() : MxViewRenderer<ViewProxyVoterViewLayout, ViewProxyVoterViewState> {
    override fun layout(layout: ViewProxyVoterViewLayout, state: ViewProxyVoterViewState): Unit = when (state.view) {
        ViewProxyVoterViewState.View.Idle -> {
        }
        ViewProxyVoterViewState.View.OnProgress -> {
            layout.showProgress()
        }
        ViewProxyVoterViewState.View.OnError -> {
            layout.showError()
        }
        is ViewProxyVoterViewState.View.Populate -> {
            layout.populate(state.view.proxyVoterDetails)
        }
        is ViewProxyVoterViewState.View.OnInvalidUrl -> {
            layout.invalidUrl(state.view.url)
        }
        is ViewProxyVoterViewState.View.NavigateToUrl -> {
            layout.navigateToUrl(state.view.url)
        }
    }
}