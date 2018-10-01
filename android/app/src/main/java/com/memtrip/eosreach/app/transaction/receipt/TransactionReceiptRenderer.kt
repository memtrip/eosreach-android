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
package com.memtrip.eosreach.app.transaction.receipt

import com.memtrip.eosreach.api.transfer.ActionReceipt
import com.memtrip.eosreach.app.account.AccountFragmentPagerAdapter
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class TransferReceiptRenderAction : MxRenderAction {
    object Idle : TransferReceiptRenderAction()
    data class Populate(val actionReceipt: ActionReceipt) : TransferReceiptRenderAction()
    object NavigateToActions : TransferReceiptRenderAction()
    data class NavigateToAccount(
        val page: AccountFragmentPagerAdapter.Page
    ) : TransferReceiptRenderAction()
    data class NavigateToBlockExplorer(val transactionId: String) : TransferReceiptRenderAction()
}

interface TransferReceiptViewLayout : MxViewLayout {
    fun populate(actionReceipt: ActionReceipt)
    fun navigateToActions()
    fun navigateToAccount(page: AccountFragmentPagerAdapter.Page)
    fun navigateToBlockExplorer(transactionId: String)
}

class TransferReceiptViewRenderer @Inject internal constructor() : MxViewRenderer<TransferReceiptViewLayout, TransactionReceiptViewState> {
    override fun layout(layout: TransferReceiptViewLayout, state: TransactionReceiptViewState): Unit = when (state.view) {
        TransactionReceiptViewState.View.Idle -> {
        }
        is TransactionReceiptViewState.View.Populate -> {
            layout.populate(state.view.actionReceipt)
        }
        is TransactionReceiptViewState.View.NavigateToBlockExplorer -> {
            layout.navigateToBlockExplorer(state.view.transactionId)
        }
        TransactionReceiptViewState.View.NavigateToActions -> {
            layout.navigateToActions()
        }
        is TransactionReceiptViewState.View.NavigateToAccount -> {
            layout.navigateToAccount(state.view.page)
        }
    }
}