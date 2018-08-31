package com.memtrip.eosreach.app.account.balance

import com.memtrip.eosreach.api.balance.AccountBalance
import com.memtrip.eosreach.api.balance.AccountBalanceList
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class BalanceRenderAction : MxRenderAction {
    object Idle : BalanceRenderAction()
    data class Populate(val accountBalances: AccountBalanceList) : BalanceRenderAction()
    object NavigateToCreateAccount : BalanceRenderAction()
    data class NavigateToActions(val accountBalance: AccountBalance) : BalanceRenderAction()
}

interface BalanceViewLayout : MxViewLayout {
    fun showBalances(accountBalanceList: AccountBalanceList)
    fun showEmptyBalance()
    fun navigateToCreateAccount()
    fun navigateToActions(accountBalance: AccountBalance)
}

class BalanceViewRenderer @Inject internal constructor() : MxViewRenderer<BalanceViewLayout, BalanceViewState> {
    override fun layout(layout: BalanceViewLayout, state: BalanceViewState): Unit = when (state.view) {
        BalanceViewState.View.Idle -> {
        }
        is BalanceViewState.View.Populate -> {
            if (state.view.accountBalances.balances.isEmpty()) {
                layout.showEmptyBalance()
            } else {
                layout.showBalances(state.view.accountBalances)
            }
        }
        BalanceViewState.View.NavigateToCreateAccount -> {
            layout.navigateToCreateAccount()
        }
        is BalanceViewState.View.NavigateToActions -> {
            layout.navigateToActions(state.view.accountBalance)
        }
    }
}