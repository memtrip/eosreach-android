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
package com.memtrip.eosreach.app.account.resources

import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.price.BalanceFormatter
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ResourcesRenderAction : MxRenderAction {
    object Idle : ResourcesRenderAction()
    data class Populate(
        val eosAccount: EosAccount,
        val contractAccountBalance: ContractAccountBalance
    ) : ResourcesRenderAction()
    object NavigateToManageBandwidth : ResourcesRenderAction()
    object NavigateToManageBandwidthWithAccountName : ResourcesRenderAction()
    object NavigateToManageRam : ResourcesRenderAction()
}

interface ResourcesViewLayout : MxViewLayout {
    fun showManageResourcesNavigation()
    fun hideManageResourcesNavigation()
    fun populate(eosAccount: EosAccount)
    fun populateNetStake(staked: String)
    fun populateCpuStake(staked: String)
    fun emptyStakedResources()
    fun emptyNetStake()
    fun emptyCpuStake()
    fun populateNetDelegated(delegated: String)
    fun populateCpuDelegated(delegated: String)
    fun emptyDelegatedResources()
    fun emptyNetDelegated()
    fun emptyCpuDelegated()
    fun navigateToManageBandwidth()
    fun navigateToManageBandwidthWithAccountName()
    fun navigateToManageRam()
}

class ResourcesViewRenderer @Inject internal constructor() : MxViewRenderer<ResourcesViewLayout, ResourcesViewState> {
    override fun layout(layout: ResourcesViewLayout, state: ResourcesViewState): Unit = when (state.view) {
        ResourcesViewState.View.Idle -> {
        }
        is ResourcesViewState.View.Populate -> {

            if (state.view.contractAccountBalance.unavailable) {
                layout.hideManageResourcesNavigation()
            } else {
                layout.showManageResourcesNavigation()
            }

            val netStaked = state.view.eosAccount.netResource.staked
            val cpuStaked = state.view.eosAccount.cpuResource.staked

            if (emptyBalance(netStaked) && emptyBalance(cpuStaked)) {
                layout.emptyStakedResources()
            } else {
                if (cpuStaked != null) {
                    layout.populateCpuStake(BalanceFormatter.formatEosBalance(cpuStaked))
                } else {
                    layout.emptyCpuStake()
                }
                if (netStaked != null) {
                    layout.populateNetStake(BalanceFormatter.formatEosBalance(netStaked))
                } else {
                    layout.emptyNetStake()
                }
            }

            val netDelegated = state.view.eosAccount.netResource.delegated
            val cpuDelegated = state.view.eosAccount.cpuResource.delegated

            if (emptyBalance(netDelegated) && emptyBalance(cpuDelegated)) {
                layout.emptyDelegatedResources()
            } else {
                if (cpuDelegated != null) {
                    layout.populateCpuDelegated(BalanceFormatter.formatEosBalance(cpuDelegated))
                } else {
                    layout.emptyCpuDelegated()
                }

                if (netDelegated != null) {
                    layout.populateNetDelegated(BalanceFormatter.formatEosBalance(netDelegated))
                } else {
                    layout.emptyNetDelegated()
                }
            }

            layout.populate(state.view.eosAccount)
        }
        ResourcesViewState.View.NavigateToManageBandwidth -> {
            layout.navigateToManageBandwidth()
        }
        ResourcesViewState.View.NavigateToManageRam -> {
            layout.navigateToManageRam()
        }
        is ResourcesViewState.View.NavigateToManageBandwidthWithAccountName -> {
            layout.navigateToManageBandwidthWithAccountName()
        }
    }

    private fun emptyBalance(balance: Balance?): Boolean {
        return balance == null || balance.amount <= 0
    }
}