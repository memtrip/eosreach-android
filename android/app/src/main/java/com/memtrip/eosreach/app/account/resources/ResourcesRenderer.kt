package com.memtrip.eosreach.app.account.resources

import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.app.price.BalanceFormatter
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class ResourcesRenderAction : MxRenderAction {
    object Idle : ResourcesRenderAction()
    data class Populate(val eosAccount: EosAccount) : ResourcesRenderAction()
    object NavigateToManageBandwidth : ResourcesRenderAction()
    object NavigateToManageRam : ResourcesRenderAction()
}

interface ResourcesViewLayout : MxViewLayout {
    fun populate(eosAccount: EosAccount)
    fun populateNetStake(staked: String)
    fun emptyNetStake()
    fun populateNetDelegated(delegated: String)
    fun emptyNetDelegated()
    fun populateCpuStake(staked: String)
    fun emptyCpuStake()
    fun populateCpuDelegated(delegated: String)
    fun emptyCpuDelegated()
    fun navigateToManageBandwidth()
    fun navigateToManageRam()
}

class ResourcesViewRenderer @Inject internal constructor() : MxViewRenderer<ResourcesViewLayout, ResourcesViewState> {
    override fun layout(layout: ResourcesViewLayout, state: ResourcesViewState): Unit = when (state.view) {
        ResourcesViewState.View.Idle -> {
        }
        is ResourcesViewState.View.Populate -> {

            val netStaked = state.view.eosAccount.netResource.staked
            if (netStaked != null) {
                layout.populateNetStake(BalanceFormatter.formatEosBalance(netStaked))
            } else {
                layout.emptyNetStake()
            }

            val netDelegated = state.view.eosAccount.netResource.delegated
            if (netDelegated != null) {
                layout.populateNetDelegated(BalanceFormatter.formatEosBalance(netDelegated))
            } else {
                layout.emptyNetDelegated()
            }

            val cpuStaked = state.view.eosAccount.cpuResource.staked
            if (cpuStaked != null) {
                layout.populateCpuStake(BalanceFormatter.formatEosBalance(cpuStaked))
            } else {
                layout.emptyCpuStake()
            }

            val cpuDelegated = state.view.eosAccount.cpuResource.delegated
            if (cpuDelegated != null) {
                layout.populateCpuDelegated(BalanceFormatter.formatEosBalance(cpuDelegated))
            } else {
                layout.emptyCpuDelegated()
            }

            layout.populate(state.view.eosAccount)
        }
        ResourcesViewState.View.NavigateToManageBandwidth -> {
            layout.navigateToManageBandwidth()
        }
        ResourcesViewState.View.NavigateToManageRam -> {
            layout.navigateToManageRam()
        }
    }
}