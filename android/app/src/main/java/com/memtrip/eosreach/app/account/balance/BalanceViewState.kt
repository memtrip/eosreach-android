package com.memtrip.eosreach.app.account.balance

import com.memtrip.eosreach.api.balance.AccountBalanceList
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.mxandroid.MxViewState

data class BalanceViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        data class Populate(val accountBalances: AccountBalanceList) : View()
        object NavigateToCreateAccount : View()
        data class NavigateToActions(val contractAccountBalance: ContractAccountBalance) : View()
    }
}