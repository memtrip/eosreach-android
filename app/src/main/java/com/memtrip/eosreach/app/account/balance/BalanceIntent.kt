package com.memtrip.eosreach.app.account.balance

import com.memtrip.eosreach.api.balance.AccountBalances

import com.memtrip.mxandroid.MxViewIntent

sealed class BalanceIntent : MxViewIntent {
    data class Init(val accountBalances: AccountBalances) : BalanceIntent()
    object CreateAccount : BalanceIntent()
}