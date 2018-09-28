package com.memtrip.eosreach.app.account.resources.manage.manageram

import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.mxandroid.MxViewState

data class ManageRamViewState(
    val view: View,
    val page: ManageRamFragmentPagerAdapter.Page = ManageRamFragmentPagerAdapter.Page.BUY
) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        data class Populate(val ramPrice: Balance) : View()
        object OnRamPriceError : View()
    }
}