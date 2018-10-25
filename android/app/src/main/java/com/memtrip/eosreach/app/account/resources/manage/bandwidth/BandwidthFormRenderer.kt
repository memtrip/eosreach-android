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
package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.price.BalanceFormatter
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class BandwidthFormRenderAction : MxRenderAction {
    object Idle : BandwidthFormRenderAction()
    data class Populate(
        val contractAccountBalance: ContractAccountBalance,
        val bandwidthFormBundle: BandwidthFormBundle
    ) : BandwidthFormRenderAction()
    data class NavigateToConfirm(
        val bandwidthCommitType: BandwidthCommitType,
        val toAccount: String,
        val netAmount: String,
        val cpuAmount: String,
        val transfer: Boolean,
        val contractAccountBalance: ContractAccountBalance
    ) : BandwidthFormRenderAction()
}

interface BandwidthFormViewLayout : MxViewLayout {
    fun navigateToConfirm(bandwidthBundle: BandwidthBundle)
    fun populate(formattedBalance: String)
    fun stakeSelfAccountName(accountName: String)
    fun stakedNet(net: String)
    fun stakedCpu(cpu: String)
}

class BandwidthFormViewRenderer @Inject internal constructor() : MxViewRenderer<BandwidthFormViewLayout, BandwidthFormViewState> {
    override fun layout(layout: BandwidthFormViewLayout, state: BandwidthFormViewState): Unit = when (state.view) {
        BandwidthFormViewState.View.Idle -> {
        }
        is BandwidthFormViewState.View.Populate -> {
            if (state.view.bandwidthBundle.delegateTarget == DelegateTarget.SELF) {
                state.view.bandwidthBundle.net?.let { net ->
                    layout.stakedNet(BalanceFormatter.formatBalanceDigits(net.amount))
                }
                state.view.bandwidthBundle.cpu?.let { cpu ->
                    layout.stakedCpu(BalanceFormatter.formatBalanceDigits(cpu.amount))
                }
                state.view.bandwidthBundle.targetAccount?.let { targetAccount ->
                    layout.stakeSelfAccountName(targetAccount)
                }
            }

            val contractAccountBalance = state.view.contractAccountBalance
            layout.populate(
                "${contractAccountBalance.balance.amount} ${contractAccountBalance.balance.symbol}")
        }
        is BandwidthFormViewState.View.NavigateToConfirm -> {
            layout.navigateToConfirm(state.view.bandwidthBundle)
        }
    }
}