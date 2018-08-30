package com.memtrip.eosreach.app.account.balance

import com.memtrip.eosreach.api.balance.AccountBalances

import com.memtrip.mxandroid.MxViewState

data class BalanceViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        data class Populate(val accountBalances: AccountBalances) : View()
        object NavigateToCreateAccount : View()
    }
}