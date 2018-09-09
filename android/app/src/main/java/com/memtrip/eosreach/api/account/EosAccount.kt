package com.memtrip.eosreach.api.account

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EosAccount(
    val accountName: String,
    val netResource: EosAccountResource,
    val cpuResource: EosAccountResource,
    val ramResource: EosAccountResource,
    val eosAcconuntVote: EosAccountVote?,
    val hasVoted: Boolean = eosAcconuntVote != null
) : Parcelable