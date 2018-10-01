/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.memtrip.eosreach.app.account.balance

import com.memtrip.eosreach.api.balance.AccountBalanceList
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class BalanceRenderAction : MxRenderAction {
    object Idle : BalanceRenderAction()
    data class Populate(val accountBalances: AccountBalanceList) : BalanceRenderAction()
    data class OnAirdropError(val message: String) : BalanceRenderAction()
    object OnAirdropSuccess : BalanceRenderAction()
    object OnAirdropProgress : BalanceRenderAction()
    object NavigateToCreateAccount : BalanceRenderAction()
    data class NavigateToActions(val contractAccountBalance: ContractAccountBalance) : BalanceRenderAction()
}

interface BalanceViewLayout : MxViewLayout {
    fun showBalances(balances: List<ContractAccountBalance>)
    fun showEmptyBalance()
    fun showAirdropError(message: String)
    fun showAirdropProgress()
    fun showAirdropSuccess()
    fun navigateToCreateAccount()
    fun navigateToActions(contractAccountBalance: ContractAccountBalance)
}

class BalanceViewRenderer @Inject internal constructor() : MxViewRenderer<BalanceViewLayout, BalanceViewState> {
    override fun layout(layout: BalanceViewLayout, state: BalanceViewState): Unit = when (state.view) {
        BalanceViewState.View.Idle -> {
        }
        is BalanceViewState.View.Populate -> {
            showBalances(layout, state.accountBalances.balances)
        }
        is BalanceViewState.View.OnAirdropError -> {
            layout.showAirdropError(state.view.message)
            showBalances(layout, state.accountBalances.balances)
        }
        BalanceViewState.View.OnAirdropProgress -> {
            layout.showAirdropProgress()
        }
        BalanceViewState.View.OnAirdropSuccess -> {
            layout.showAirdropSuccess()
        }
        BalanceViewState.View.NavigateToCreateAccount -> {
            layout.navigateToCreateAccount()
        }
        is BalanceViewState.View.NavigateToActions -> {
            layout.navigateToActions(state.view.contractAccountBalance)
        }
    }

    private fun showBalances(layout: BalanceViewLayout, balances: List<ContractAccountBalance>) {
        if (balances.isEmpty()) {
            layout.showEmptyBalance()
        } else {
            layout.showBalances(balances)
        }
    }
}