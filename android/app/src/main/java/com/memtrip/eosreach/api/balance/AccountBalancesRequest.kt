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
package com.memtrip.eosreach.api.balance

import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.eosprice.EosPrice

import io.reactivex.Single
import okhttp3.ResponseBody

interface AccountBalanceRequest {

    fun getBalance(
        contractName: String,
        accountName: String,
        symbol: String,
        eosPrice: EosPrice
    ): Single<Result<AccountBalanceList, BalanceError>>
}

sealed class BalanceError : ApiError {
    object Generic : BalanceError()
    data class FailedRetrievingBalance(
        val code: Int,
        val body: ResponseBody?
    ) : BalanceError()
}