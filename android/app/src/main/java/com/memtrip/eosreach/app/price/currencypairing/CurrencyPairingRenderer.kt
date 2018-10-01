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
package com.memtrip.eosreach.app.price.currencypairing

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class CurrencyPairingRenderAction : MxRenderAction {
    object Idle : CurrencyPairingRenderAction()
    object OnProgress : CurrencyPairingRenderAction()
    data class OnError(val message: String) : CurrencyPairingRenderAction()
    object OnSuccess : CurrencyPairingRenderAction()
}

interface CurrencyPairingViewLayout : MxViewLayout {
    fun showProgress()
    fun showError(message: String)
    fun onSuccess()
}

class CurrencyPairingViewRenderer @Inject internal constructor() : MxViewRenderer<CurrencyPairingViewLayout, CurrencyPairingViewState> {
    override fun layout(layout: CurrencyPairingViewLayout, state: CurrencyPairingViewState): Unit = when (state.view) {
        CurrencyPairingViewState.View.Idle -> {
        }
        CurrencyPairingViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is CurrencyPairingViewState.View.OnError -> {
            layout.showError(state.view.message)
        }
        CurrencyPairingViewState.View.OnSuccess -> {
            layout.onSuccess()
        }
    }
}