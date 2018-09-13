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