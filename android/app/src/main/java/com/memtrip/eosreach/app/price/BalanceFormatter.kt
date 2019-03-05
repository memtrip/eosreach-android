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
package com.memtrip.eosreach.app.price

import com.memtrip.eosreach.api.balance.Balance
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object BalanceFormatter {

    fun deserialize(balance: String): Balance {
        val parts = balance.split(" ")
        return Balance(parts[0].toDouble(), parts[1])
    }

    fun create(balance: String, symbol: String): Balance {
        return if (balance.isEmpty()) {
            create(0.0, symbol)
        } else {
            create(balance.toDouble(), symbol)
        }
    }

    fun create(balance: Double, symbol: String): Balance {
        return Balance(balance, symbol)
    }

    fun formatEosBalance(balance: Double, symbol: String): String {
        return formatEosBalance(balance.toString(), symbol)
    }

    fun formatEosBalance(balance: String, symbol: String): String {
        return formatEosBalance(create(balance, symbol))
    }

    fun formatEosBalance(balance: Balance): String {
        val value = formatBalanceDigits(balance.amount)
        return "$value ${balance.symbol}"
    }

    fun formatBalanceDigits(amount: Double): String {

        return with(DecimalFormat("0.0000")) {
            roundingMode = RoundingMode.CEILING
            decimalFormatSymbols = DecimalFormatSymbols().apply {
                decimalSeparator = '.'
            }
            this
        }.format(amount)
    }
}