package com.memtrip.eosreach.api.account

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EosAccountResource(
    val used: Long,
    val available: Long
) : Parcelable