package com.memtrip.eosreach.app.transfer.form

import com.memtrip.mxandroid.MxViewIntent

sealed class TransferFormIntent : MxViewIntent {
    object Init : TransferFormIntent()
    data class SubmitForm(val transferFormData: TransferFormData) : TransferFormIntent()
}