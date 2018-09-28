package com.memtrip.eosreach.app.account.resources.manage.manageram

import com.memtrip.mxandroid.MxViewState

data class RamFormViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        data class UpdateCostPerKiloByte(val eosCost: String) : View()
        data class NavigateToConfirmRamForm(
            val kilobytes: String,
            val ramCommitType: RamCommitType
        ) : View()
        object EmptyRamError : View()
    }
}