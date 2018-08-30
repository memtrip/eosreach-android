package com.memtrip.eosreach.utils

import com.memtrip.eosreach.api.balance.Balance

class BalanceParser {

    fun accountBalances(balances: List<String>): List<Balance> {
        return balances.map { pull(it) }
    }

    fun pull(balance: String): Balance {
        val parts = balance.split(" ")
        return Balance(parts[0].toDouble(), parts[1])
    }
}