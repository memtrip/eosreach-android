package com.memtrip.eosreach.app.transfer.confirm

import com.memtrip.eosreach.api.transfer.ActionReceipt
import com.memtrip.eosreach.app.transfer.form.TransferFormData

import com.memtrip.mxandroid.MxViewState

data class TransferConfirmViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        data class Populate(val transferFormData: TransferFormData) : View()
        object OnProgress : View()
        data class OnSuccess(val transferReceipt: ActionReceipt) : View()
        data class OnError(val message: String, val log: String) : View()
        data class ViewLog(val log: String) : View()
    }
}