package com.memtrip.eosreach.app.account.actions

import com.memtrip.eosreach.api.actions.model.AccountAction
import com.memtrip.eosreach.api.balance.AccountBalance
import com.memtrip.mxandroid.MxViewIntent

sealed class ActionsIntent : MxViewIntent {
    object Idle : ActionsIntent()
    data class Init(val accountBalance: AccountBalance) : ActionsIntent()
    data class Retry(val accountBalance: AccountBalance) : ActionsIntent()
    data class NavigateToViewAction(val accountAction: AccountAction) : ActionsIntent()
}