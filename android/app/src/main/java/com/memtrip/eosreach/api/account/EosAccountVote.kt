package com.memtrip.eosreach.api.account

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EosAccountVote(
    val proxyVoterAccountName: String,
    val producers: List<String>,
    val staked: Double,
    val lastVoteWeight: Double,
    val proxiedVoteWeight: Double,
    val isProxyVote: Boolean
) : Parcelable