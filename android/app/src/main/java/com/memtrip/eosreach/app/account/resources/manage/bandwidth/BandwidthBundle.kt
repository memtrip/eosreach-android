package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.os.Parcelable
import com.memtrip.eosreach.api.balance.Balance
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BandwidthBundle(
    val bandwidthCommitType: BandwidthCommitType,
    val netAmount: Balance,
    val cpuAmount: Balance,
    val fromAccount: String
) : Parcelable