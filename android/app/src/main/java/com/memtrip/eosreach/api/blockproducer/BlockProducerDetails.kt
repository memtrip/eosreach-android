package com.memtrip.eosreach.api.blockproducer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BlockProducerDetails(
    val owner: String,
    val candidateName: String,
    val apiUrl: String,
    val website: String,
    val codeOfConductUrl: String,
    val ownershipDisclosureUrl: String,
    val email: String,
    val logo256: String?
) : Parcelable