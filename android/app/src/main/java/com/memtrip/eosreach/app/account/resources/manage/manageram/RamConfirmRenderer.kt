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
package com.memtrip.eosreach.app.account.resources.manage.manageram

import com.memtrip.eosreach.api.transfer.ActionReceipt
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class RamConfirmRenderAction : MxRenderAction {
    object Idle : RamConfirmRenderAction()
    object Populate : RamConfirmRenderAction()
    object OnProgress : RamConfirmRenderAction()
    data class OnSuccess(val transferReceipt: ActionReceipt) : RamConfirmRenderAction()
    data class OnError(val log: String) : RamConfirmRenderAction()
}

interface RamConfirmViewLayout : MxViewLayout {
    fun populate()
    fun showProgress()
    fun onSuccess(transferReceipt: ActionReceipt)
    fun showError(log: String)
}

class RamConfirmViewRenderer @Inject internal constructor() : MxViewRenderer<RamConfirmViewLayout, RamConfirmViewState> {
    override fun layout(layout: RamConfirmViewLayout, state: RamConfirmViewState): Unit = when (state.view) {
        RamConfirmViewState.View.Idle -> {
        }
        RamConfirmViewState.View.OnProgress -> {
            layout.showProgress()
        }
        is RamConfirmViewState.View.OnError -> {
            layout.showError(state.view.log)
        }
        RamConfirmViewState.View.Populate -> {
            layout.populate()
        }
        is RamConfirmViewState.View.OnSuccess -> {
            layout.onSuccess(state.view.transferReceipt)
        }
    }
}