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