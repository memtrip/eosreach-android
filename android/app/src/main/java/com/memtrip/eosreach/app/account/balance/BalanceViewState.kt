package com.memtrip.eosreach.app.account.balance

import com.memtrip.eosreach.api.balance.AccountBalanceList
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.mxandroid.MxViewState

data class BalanceViewState(
    val view: View,
    val accountBalances: AccountBalanceList = AccountBalanceList(ArrayList())
) : MxViewState {

    sealed class View {
        object Idle : View()
        object Populate : View()
        data class OnAirdropError(val message: String) : View()
        object OnAirdropProgress : View()
        object OnAirdropSuccess : View()
        object NavigateToCreateAccount : View()
        data class NavigateToActions(val contractAccountBalance: ContractAccountBalance) : View()
    }
}