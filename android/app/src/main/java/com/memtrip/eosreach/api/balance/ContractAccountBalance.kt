package com.memtrip.eosreach.api.balance

import android.os.Parcelable
import com.memtrip.eosreach.api.eosprice.EosPrice
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContractAccountBalance(
    val contractName: String,
    val accountName: String,
    val balance: Balance,
    val exchangeRate: EosPrice
) : Parcelable