package com.memtrip.eosreach.app.blockproducerlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BlockProducerBundle(
    val accountName: String,
    val candidateName: String,
    val apiUrl: String,
    val logoUrl: String?
) : Parcelable