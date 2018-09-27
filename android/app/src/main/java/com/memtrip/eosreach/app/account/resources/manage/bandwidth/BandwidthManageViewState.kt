package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.mxandroid.MxViewState

data class BandwidthManageViewState(
    val view: View,
    val page: BandwidthManageFragmentPagerAdapter.Page = BandwidthManageFragmentPagerAdapter.Page.DELEGATE
) : MxViewState {

    sealed class View {
        object Idle : View()
        object Populate : View()
    }
}