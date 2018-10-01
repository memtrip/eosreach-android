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

import com.memtrip.eosreach.api.eosprice.EosPrice
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Currency

object CurrencyPairFormatter {

    fun formatAmountCurrencyPairValue(cryptoAmount: Double, eosPrice: EosPrice): String {
        return if (eosPrice.unavailable) { "" } else {
            val fiatPrice = cryptoAmount * eosPrice.value
            formatCurrencyPairValue(fiatPrice, eosPrice.currency)
        }
    }

    private fun formatCurrencyPairValue(price: Double, currencyCode: String): String {
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
        return if (isCrypto) {
            BigDecimal(price).setScale(5, RoundingMode.CEILING).toPlainString()
        } else {
            BigDecimal(price).setScale(2, RoundingMode.CEILING).toPlainString()
        }
    }
}