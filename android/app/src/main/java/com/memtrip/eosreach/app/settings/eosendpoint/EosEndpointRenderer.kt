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
package com.memtrip.eosreach.app.settings.eosendpoint

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class EosEndpointRenderAction : MxRenderAction {
    object Idle : EosEndpointRenderAction()
    object OnProgress : EosEndpointRenderAction()
    data class OnError(val message: String) : EosEndpointRenderAction()
    object OnSuccess : EosEndpointRenderAction()
    object NavigateToBlockProducerList : EosEndpointRenderAction()
}

interface EosEndpointViewLayout : MxViewLayout {
    fun showProgress()
    fun showError(message: String)
    fun currentUrl(url: String)
    fun onSuccess()
    fun navigateToBlockProducerList()
}

class EosEndpointViewRenderer @Inject internal constructor() : MxViewRenderer<EosEndpointViewLayout, EosEndpointViewState> {
    override fun layout(layout: EosEndpointViewLayout, state: EosEndpointViewState) {
        layout.currentUrl(state.endpointUrl)
        layoutView(layout, state)
    }

    private fun layoutView(layout: EosEndpointViewLayout, state: EosEndpointViewState): Unit = when (state.view) {
        EosEndpointViewState.View.Idle -> {
        }
        EosEndpointViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is EosEndpointViewState.View.OnError -> {
            layout.showError(state.view.message)
        }
        EosEndpointViewState.View.OnSuccess -> {
            layout.onSuccess()
        }
        EosEndpointViewState.View.NavigateToBlockProducerList -> {
            layout.navigateToBlockProducerList()
        }
    }
}