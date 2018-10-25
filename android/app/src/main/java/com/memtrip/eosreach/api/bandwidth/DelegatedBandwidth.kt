package com.memtrip.eosreach.api.bandwidth

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DelegatedBandwidth(
    val accountName: String,
    val netWeight: String,
    val cpuWeight: String
) : Parcelable