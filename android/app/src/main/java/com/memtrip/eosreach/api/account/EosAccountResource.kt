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