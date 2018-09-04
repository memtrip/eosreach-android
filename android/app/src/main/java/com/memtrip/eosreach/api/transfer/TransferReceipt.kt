package com.memtrip.eosreach.api.transfer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransferReceipt(
    val transactionId: String
) : Parcelable