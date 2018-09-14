package com.memtrip.eosreach.app.account.resources.manage.manageram

import com.memtrip.eosreach.api.account.EosAccount

import com.memtrip.mxandroid.MxViewState

data class ManageRamViewState(
    val view: View,
    val page: ManageRamFragmentPagerAdapter.Page = ManageRamFragmentPagerAdapter.Page.BUY
) : MxViewState {

    sealed class View {
        object Idle : ManageRamViewState.View()
        data class Populate(val eosAccount: EosAccount) : ManageRamViewState.View()
    }
}