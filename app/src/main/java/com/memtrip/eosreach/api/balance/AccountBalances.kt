package com.memtrip.eosreach.api.balance

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccountBalances(
    val balances: List<Balance>
) : Parcelable