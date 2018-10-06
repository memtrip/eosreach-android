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
package com.memtrip.eosreach.app.transaction.log

import com.memtrip.eosreach.db.transaction.TransactionLogEntity
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ViewConfirmedTransactionsRenderAction : MxRenderAction {
    object Idle : ViewConfirmedTransactionsRenderAction()
    object OnProgress : ViewConfirmedTransactionsRenderAction()
    object OnError : ViewConfirmedTransactionsRenderAction()
    object Empty : ViewConfirmedTransactionsRenderAction()
    data class Populate(val transactionLogEntities: List<TransactionLogEntity>) : ViewConfirmedTransactionsRenderAction()
    data class NavigateToBlockExplorer(val transactionId: String) : ViewConfirmedTransactionsRenderAction()
}

interface ViewConfirmedTransactionsViewLayout : MxViewLayout {
    fun populate(transactionLogEntities: List<TransactionLogEntity>)
    fun empty()
    fun showProgress()
    fun showError()
    fun navigateToBlockExplorer(transactionId: String)
}

class ViewConfirmedTransactionsViewRenderer @Inject internal constructor() : MxViewRenderer<ViewConfirmedTransactionsViewLayout, ViewConfirmedTransactionsViewState> {
    override fun layout(layout: ViewConfirmedTransactionsViewLayout, state: ViewConfirmedTransactionsViewState): Unit = when (state.view) {
        ViewConfirmedTransactionsViewState.View.Idle -> {
        }
        ViewConfirmedTransactionsViewState.View.OnProgress -> {
            layout.showProgress()
        }
        ViewConfirmedTransactionsViewState.View.OnError -> {
            layout.showError()
        }
        ViewConfirmedTransactionsViewState.View.Empty -> {
            layout.empty()
        }
        is ViewConfirmedTransactionsViewState.View.Populate -> {
            layout.populate(state.view.transactionLogEntities)
        }
        is ViewConfirmedTransactionsViewState.View.NavigateToBlockExplorer -> {
            layout.navigateToBlockExplorer(state.view.transactionId)
        }
    }
}