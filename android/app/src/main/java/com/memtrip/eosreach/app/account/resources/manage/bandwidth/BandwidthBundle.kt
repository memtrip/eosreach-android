package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BandwidthBundle(
    val fromAccount: String,
    val netAmount: String,
    val cpuAmount: String,
    val bandwidthCommitType: BandwidthCommitType
) : Parcelable