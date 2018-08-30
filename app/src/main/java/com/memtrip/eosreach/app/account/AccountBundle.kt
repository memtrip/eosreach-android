package com.memtrip.eosreach.app.account

import android.os.Parcelable
import com.memtrip.eosreach.api.balance.Balance
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccountBundle(
    val accountName: String,
    val balance: Double?,
    val symbol: String?
) : Parcelable {

    fun balance(): Balance? {
        if (balance != null && symbol != null) {
            return Balance(balance, symbol)
        } else {
            return null
        }
    }
}