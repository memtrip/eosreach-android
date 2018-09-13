package com.memtrip.eosreach.app.transfer.form

import android.os.Parcelable
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransferFormData(
    val contractAccountBalance: ContractAccountBalance,
    val toAccountName: String,
    val amount: String,
    val memo: String
) : Parcelable