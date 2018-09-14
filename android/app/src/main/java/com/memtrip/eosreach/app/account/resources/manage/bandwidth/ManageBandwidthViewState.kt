package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.eosreach.api.account.EosAccount

import com.memtrip.mxandroid.MxViewState

data class ManageBandwidthViewState(
    val view: View,
    val page: ManageBandwidthFragmentPagerAdapter.Page = ManageBandwidthFragmentPagerAdapter.Page.DELEGATE
) : MxViewState {

    sealed class View {
        object Idle : View()
        data class Populate(val eosAccount: EosAccount) : View()
    }
}