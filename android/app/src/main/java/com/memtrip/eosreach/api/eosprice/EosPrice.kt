package com.memtrip.eosreach.api.eosprice

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EosPrice(
    val value: Double,
    val currency: String,
    val outOfDate: Boolean = false,
    val unavailable: Boolean = false
) : Parcelable {

    companion object {
        fun unavailable(): EosPrice {
            return EosPrice(
                (-1).toDouble(),
                "-",
                false,
                true
            )
        }
    }
}