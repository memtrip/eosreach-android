package com.memtrip.eosreach.api.balance

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Balance(
    val amount: Double,
    val symbol: String
) : Parcelable