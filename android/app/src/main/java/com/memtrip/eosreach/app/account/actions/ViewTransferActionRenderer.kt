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
package com.memtrip.eosreach.app.account.actions

import com.memtrip.eosreach.api.actions.model.AccountAction
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ViewTransferActionRenderAction : MxRenderAction {
    data class Populate(val transferAccountAction: AccountAction.Transfer) : ViewTransferActionRenderAction()
    object Idle : ViewTransferActionRenderAction()
    data class ViewTransactionBlockExplorer(val transactionId: String) : ViewTransferActionRenderAction()
}

interface ViewTransferActionViewLayout : MxViewLayout {
    fun populate(accountAction: AccountAction.Transfer)
    fun viewTransactionBlockExplorer(transactionId: String)
}

class ViewTransferActionViewRenderer @Inject internal constructor() : MxViewRenderer<ViewTransferActionViewLayout, ViewTransferActionViewState> {
    override fun layout(layout: ViewTransferActionViewLayout, state: ViewTransferActionViewState): Unit = when (state.view) {
        ViewTransferActionViewState.View.Idle -> {
        }
        is ViewTransferActionViewState.View.Populate -> {
            layout.populate(state.view.accountAction)
        }
        is ViewTransferActionViewState.View.ViewTransactionBlockExplorer -> {
            layout.viewTransactionBlockExplorer(state.view.transactionId)
        }
    }
}