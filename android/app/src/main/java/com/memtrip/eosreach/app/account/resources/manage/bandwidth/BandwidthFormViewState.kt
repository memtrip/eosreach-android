package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.mxandroid.MxViewState

data class BandwidthFormViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        data class Populate(val contractAccountBalance: ContractAccountBalance) : View()
        data class NavigateToConfirm(val bandwidthBundle: BandwidthBundle) : View()
    }
}