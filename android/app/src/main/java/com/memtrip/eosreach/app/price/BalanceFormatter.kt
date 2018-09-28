package com.memtrip.eosreach.app.price

import com.memtrip.eosreach.api.balance.Balance
import java.math.RoundingMode
import java.text.DecimalFormat

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
        val value = with (DecimalFormat("0.0000")) {
            roundingMode = RoundingMode.CEILING
            this
        }.format(balance.amount)

        return "$value ${balance.symbol}"
    }
}