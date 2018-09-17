package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.mxandroid.MxRenderAction
import com.memtrip.mxandroid.MxViewLayout
import com.memtrip.mxandroid.MxViewRenderer
import javax.inject.Inject

sealed class BandwidthFormRenderAction : MxRenderAction {
    object Idle : BandwidthFormRenderAction()
    data class Populate(val contractAccountBalance: ContractAccountBalance) : BandwidthFormRenderAction()
    data class NavigateToConfirm(
        val bandwidthCommitType: BandwidthCommitType,
        val netAmount: String,
        val cpuAmount: String,
        val fromAccount: String,
        val contractAccountBalance: ContractAccountBalance
    ) : BandwidthFormRenderAction()
}

interface BandwidthFormViewLayout : MxViewLayout {
    fun navigateToConfirm(bandwidthBundle: BandwidthBundle)
    fun populate(formattedBalance: String)
}

class BandwidthFormViewRenderer @Inject internal constructor() : MxViewRenderer<BandwidthFormViewLayout, BandwidthFormViewState> {
    override fun layout(layout: BandwidthFormViewLayout, state: BandwidthFormViewState): Unit = when (state.view) {
        BandwidthFormViewState.View.Idle -> {
        }
        is BandwidthFormViewState.View.Populate -> {
            val contractAccountBalance = state.view.contractAccountBalance
            layout.populate(
                "${contractAccountBalance.balance.amount} ${contractAccountBalance.balance.symbol}")
        }
        is BandwidthFormViewState.View.NavigateToConfirm -> {
            layout.navigateToConfirm(state.view.bandwidthBundle)
        }
    }
}