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
package com.memtrip.eosreach.uikit

import android.content.Context
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.price.BalanceFormatter
import com.memtrip.eosreach.app.price.CurrencyPairFormatter

interface BalanceDetailsLayout {

    fun formatBalance(
        balance: Balance,
        contractAccountBalance: ContractAccountBalance,
        context: Context
    ): String

    fun amountAndCurrencyPairing(
        formattedAmount: String,
        currencyPairBalance: String,
        context: Context
    ): String

    fun amountOnly(
        formattedAmount: String,
        context: Context
    ): String
}

class BalanceDetailsLayoutImpl : BalanceDetailsLayout {

    override fun formatBalance(
        balance: Balance,
        contractAccountBalance: ContractAccountBalance,
        context: Context
    ): String {
        val currencyPairBalance = CurrencyPairFormatter.formatAmountCurrencyPairValue(
            balance.amount, contractAccountBalance.exchangeRate)

        val formattedAmount = BalanceFormatter.formatEosBalance(balance)

        return if (currencyPairBalance.isNotEmpty()) {
            amountAndCurrencyPairing(formattedAmount, currencyPairBalance, context)
        } else {
            amountOnly(formattedAmount, context)
        }
    }

    override fun amountAndCurrencyPairing(
        formattedAmount: String,
        currencyPairBalance: String,
        context: Context
    ): String {
        return context.getString(
            R.string.uikit_details_layout_amount_with_currency_pair,
            formattedAmount,
            currencyPairBalance)
    }

    override fun amountOnly(
        formattedAmount: String,
        context: Context
    ): String {
        return context.getString(
            R.string.uikit_details_layout_amount,
            formattedAmount)
    }
}