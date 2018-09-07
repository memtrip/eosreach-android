package com.memtrip.eosreach.app.transfer.confirm

import com.memtrip.eosreach.app.transfer.form.TransferFormData
import com.memtrip.mxandroid.MxViewIntent

sealed class TransferConfirmIntent : MxViewIntent {
    object Idle : TransferConfirmIntent()
    data class Init(val transferFormData: TransferFormData) : TransferConfirmIntent()
    data class Transfer(val transferRequestData: TransferRequestData) : TransferConfirmIntent()
    data class ViewLog(val log: String) : TransferConfirmIntent()
}