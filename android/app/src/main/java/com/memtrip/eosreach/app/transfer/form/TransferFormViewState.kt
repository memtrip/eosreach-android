package com.memtrip.eosreach.app.transfer.form

import com.memtrip.mxandroid.MxViewState

data class TransferFormViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        data class OnComplete(val transferFormData: TransferFormData) : View()
        data class OnValidationError(val message: String) : View()
    }
}