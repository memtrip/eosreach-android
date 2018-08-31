package com.memtrip.eosreach.app.account.actions

import com.memtrip.eosreach.api.balance.AccountBalance
import com.memtrip.mxandroid.MxViewIntent

sealed class ActionsIntent : MxViewIntent {
    data class Init(val accountBalance: AccountBalance) : ActionsIntent()
}