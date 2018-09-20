package com.memtrip.eosreach.app.account

import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.balance.AccountBalanceList
import com.memtrip.eosreach.api.eosprice.EosPrice

data class AccountView(
    val eosPrice: EosPrice?,
    val eosAccount: EosAccount?,
    val balances: AccountBalanceList?,
    val error: Error? = null,
    val success: Boolean = error == null
) {

    sealed class Error {
        object FetchingAccount : Error()
        object FetchingBalances : Error()
    }

    companion object {

        fun error(type: Error): AccountView {
            return AccountView(null, null, null, type)
        }

        fun success(eosPrice: EosPrice?, eosAccount: EosAccount?, balances: AccountBalanceList?): AccountView {
            return AccountView(eosPrice, eosAccount, balances)
        }
    }
}