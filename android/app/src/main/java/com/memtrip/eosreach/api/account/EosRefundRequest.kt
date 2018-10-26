package com.memtrip.eosreach.api.account

import android.os.Parcelable
import com.memtrip.eosreach.api.balance.Balance
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class EosRefundRequest(
    val owner: String,
    val requestTime: Date,
    val net: Balance,
    val cpu: Balance
) : Parcelable