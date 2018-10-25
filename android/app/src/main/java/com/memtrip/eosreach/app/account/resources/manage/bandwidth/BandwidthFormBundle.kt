package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.os.Parcelable
import com.memtrip.eosreach.api.balance.Balance
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BandwidthFormBundle(
    val targetAccount: String? = null,
    val delegateTarget: DelegateTarget = DelegateTarget.THIRD_PARTY,
    val net: Balance? = null,
    val cpu: Balance? = null
) : Parcelable