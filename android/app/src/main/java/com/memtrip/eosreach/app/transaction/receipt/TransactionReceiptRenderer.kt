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