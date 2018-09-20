package com.memtrip.eosreach.app.account.actions

import com.memtrip.eosreach.api.actions.model.AccountAction
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.mxandroid.MxViewIntent

sealed class ActionsIntent : MxViewIntent {
    object Idle : ActionsIntent()
    data class Init(
        val contractAccountBalance: ContractAccountBalance,
        val startingPosition: Int = -500
    ) : ActionsIntent()
    data class Retry(
        val contractAccountBalance: ContractAccountBalance,
        val startingPosition: Int = -500
    ) : ActionsIntent()
    data class LoadMoreActions(
        val contractAccountBalance: ContractAccountBalance,
        val lastItem: AccountAction
    ) : ActionsIntent()
    data class NavigateToViewAction(val accountAction: AccountAction) : ActionsIntent()
    data class NavigateToTransfer(val contractAccountBalance: ContractAccountBalance) : ActionsIntent()
}