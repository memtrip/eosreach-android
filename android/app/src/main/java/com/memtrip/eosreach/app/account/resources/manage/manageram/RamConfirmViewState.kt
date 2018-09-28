package com.memtrip.eosreach.app.account.resources.manage.manageram

import com.memtrip.eosreach.api.transfer.ActionReceipt
import com.memtrip.mxandroid.MxViewState

data class RamConfirmViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object Populate : View()
        object OnProgress : View()
        data class OnSuccess(val transferReceipt: ActionReceipt) : View()
        data class OnError(val log: String) : View()
    }
}