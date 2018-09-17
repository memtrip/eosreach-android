package com.memtrip.eosreach.api.transfer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ActionReceipt(
    val transactionId: String,
    val authorizingAccountName: String
) : Parcelable