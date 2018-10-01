/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.memtrip.eosreach.api.balance

import android.os.Parcelable
import com.memtrip.eosreach.api.eosprice.EosPrice
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContractAccountBalance(
    val contractName: String,
    val accountName: String,
    val balance: Balance,
    val exchangeRate: EosPrice,
    val unavailable: Boolean = false
) : Parcelable {

    companion object {
        fun unavailable(): ContractAccountBalance {
            return ContractAccountBalance(
                "unavailable",
                "unavailable",
                Balance(0.0, "unavailable"),
                EosPrice.unavailable(),
                true)
        }
    }
}