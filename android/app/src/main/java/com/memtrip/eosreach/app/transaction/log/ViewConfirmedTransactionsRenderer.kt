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
    data class Populate(val transactionLogEntities: List<TransactionLogEntity>) : ViewConfirmedTransactionsRenderAction()
    data class NavigateToBlockExplorer(val transactionId: String) : ViewConfirmedTransactionsRenderAction()
}

interface ViewConfirmedTransactionsViewLayout : MxViewLayout {
    fun populate(transactionLogEntities: List<TransactionLogEntity>)
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
        is ViewConfirmedTransactionsViewState.View.Populate -> {
            layout.populate(state.view.transactionLogEntities)
        }
        is ViewConfirmedTransactionsViewState.View.NavigateToBlockExplorer -> {
            layout.navigateToBlockExplorer(state.view.transactionId)
        }
    }
}