package com.memtrip.eosreach.app.transfer.form

import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.mxandroid.MxViewIntent

sealed class TransferFormIntent : MxViewIntent {
    object Idle : TransferFormIntent()
    data class Init(val contractAccountBalance: ContractAccountBalance) : TransferFormIntent()
    data class SubmitForm(val transferFormData: TransferFormData) : TransferFormIntent()
}