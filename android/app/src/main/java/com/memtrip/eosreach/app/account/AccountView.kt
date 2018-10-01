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