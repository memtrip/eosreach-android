package com.memtrip.eosreach.api.account

import android.os.Parcelable
import com.memtrip.eosreach.api.balance.Balance
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EosAccountResource(
    val used: Long,
    val available: Long,
    val staked: Balance? = null,
    val delegated: Balance? = null
) : Parcelable