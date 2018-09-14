package com.memtrip.eosreach.app.account

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccountBundle(val accountName: String) : Parcelable