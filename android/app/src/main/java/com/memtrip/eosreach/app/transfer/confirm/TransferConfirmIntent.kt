package com.memtrip.eosreach.app.transfer.confirm

import com.memtrip.eosreach.app.transfer.form.TransferFormData
import com.memtrip.mxandroid.MxViewIntent

sealed class TransferConfirmIntent : MxViewIntent {
    data class Init(val transferFormData: TransferFormData) : TransferConfirmIntent()
    data class Transfer(val transferRequestData: TransferRequestData) : TransferConfirmIntent()
}