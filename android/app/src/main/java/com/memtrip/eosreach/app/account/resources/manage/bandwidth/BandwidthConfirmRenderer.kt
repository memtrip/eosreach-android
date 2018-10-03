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
package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class BandwidthConfirmRenderAction : MxRenderAction {
    object Idle : BandwidthConfirmRenderAction()
    data class Populate(val bandwidthBundle: BandwidthBundle) : BandwidthConfirmRenderAction()
    object OnProgress : BandwidthConfirmRenderAction()
    data class OnGenericError(val message: String) : BandwidthConfirmRenderAction()
    data class OnTransactionError(val message: String, val log: String) : BandwidthConfirmRenderAction()
    data class OnSuccess(val transactionId: String) : BandwidthConfirmRenderAction()
}

interface BandwidthConfirmViewLayout : MxViewLayout {
    fun populate(bandwidthBundle: BandwidthBundle)
    fun showProgress()
    fun showGenericError(message: String)
    fun showTransactionError(message: String, log: String)
    fun showSuccess(transactionId: String)
}

class BandwidthConfirmViewRenderer @Inject internal constructor() : MxViewRenderer<BandwidthConfirmViewLayout, BandwidthConfirmViewState> {
    override fun layout(layout: BandwidthConfirmViewLayout, state: BandwidthConfirmViewState): Unit = when (state.view) {
        BandwidthConfirmViewState.View.Idle -> {
        }
        is BandwidthConfirmViewState.View.Populate -> {
            layout.populate(state.view.bandwidthBundle)
        }
        BandwidthConfirmViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is BandwidthConfirmViewState.View.OnGenericError -> {
            layout.showGenericError(state.view.message)
        }
        is BandwidthConfirmViewState.View.OnTransactionError -> {
            layout.showTransactionError(state.view.message, state.view.log)
        }
        is BandwidthConfirmViewState.View.OnSuccess -> {
            layout.showSuccess(state.view.transactionId)
        }
    }
}