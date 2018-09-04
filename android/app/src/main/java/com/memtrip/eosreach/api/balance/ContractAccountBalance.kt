package com.memtrip.eosreach.api.balance

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContractAccountBalance(
    val contractName: String,
    val accountName: String,
    val balance: Balance
) : Parcelable