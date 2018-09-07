package com.memtrip.eosreach.app.transaction.log

import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class TransactionLogRenderAction : MxRenderAction {
    object Idle : TransactionLogRenderAction()
    data class ShowLog(val log: String) : TransactionLogRenderAction()
}

interface TransactionLogViewLayout : MxViewLayout {
    fun showLog(log: String)
}

class TransactionLogViewRenderer @Inject internal constructor() : MxViewRenderer<TransactionLogViewLayout, TransactionLogViewState> {
    override fun layout(layout: TransactionLogViewLayout, state: TransactionLogViewState): Unit = when (state.view) {
        TransactionLogViewState.View.Idle -> {
        }
        is TransactionLogViewState.View.ShowLog -> {
            layout.showLog(state.view.log)
        }
    }
}