package com.memtrip.eosreach.app.price

import com.memtrip.eosreach.api.balance.Balance
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Currency

class BalanceParser private constructor() {

    companion object {

        fun accountBalances(balances: List<String>): List<Balance> {
            return balances.map { deserialize(it) }
        }

        fun create(balance: String, symbol: String): Balance {
            return Balance(balance.toDouble(), symbol)
        }

        fun deserialize(balance: String): Balance {
            val parts = balance.split(" ")
            return Balance(parts[0].toDouble(), parts[1])
        }

        fun serializeForEosApiRequest(balance: Balance): String {
            return "${balance.amount} ${balance.symbol}"
        }

        fun format(balance: Balance): String {
            return format(balance.amount, balance.symbol)
        }

        fun format(price: Double, currencyCode: String): String {
            val currencySymbol = currencySymbol(currencyCode)
            val priceString = formatBalanceString(price, currencySymbol.isCrypto)
            return "${currencySymbol.symbol}$priceString"
        }

        private fun currencySymbol(symbol: String): CurrencySymbol = try {
            CurrencySymbol(Currency.getInstance(symbol).symbol, false)
        } catch (e: RuntimeException) {
            CurrencySymbol("$symbol ", true) // fallback for crypto pairings
        }

        private fun formatBalanceString(price: Double, isCrypto: Boolean): String {
            if (isCrypto) {
                return BigDecimal(price).setScale(5, RoundingMode.CEILING).toPlainString()
            } else {
                return BigDecimal(price).setScale(2, RoundingMode.CEILING).toPlainString()
            }
        }
    }
}