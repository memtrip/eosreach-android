package com.memtrip.eosreach.app.account.actions

import com.memtrip.eosreach.api.actions.model.AccountAction
import com.memtrip.mxandroid.MxViewState

data class ViewTransferActionViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        data class Populate(val accountAction: AccountAction.Transfer) : View()
        data class ViewTransactionBlockExplorer(val transactionId: String) : View()
    }
}