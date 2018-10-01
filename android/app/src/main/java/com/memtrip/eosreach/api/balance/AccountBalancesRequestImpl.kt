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

import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.model.contract.request.GetCurrencyBalance
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.eosprice.EosPrice
import com.memtrip.eosreach.app.price.BalanceFormatter

import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class AccountBalanceRequestImp @Inject internal constructor(
    private val chainApi: ChainApi,
    private val rxSchedulers: RxSchedulers
) : AccountBalanceRequest {

    override fun getBalance(
        contractName: String,
        accountName: String,
        symbol: String,
        eosPrice: EosPrice
    ): Single<Result<AccountBalanceList, BalanceError>> {

        return chainApi.getCurrencyBalance(
            GetCurrencyBalance(
                contractName,
                accountName,
                symbol))
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.main())
            .map {
                if (it.isSuccessful) {
                    Result(AccountBalanceList(getContractAccountBalances(
                        contractName,
                        accountName,
                        it.body()!!,
                        eosPrice)
                    ))
                } else {
                    Result<AccountBalanceList, BalanceError>(
                        BalanceError.FailedRetrievingBalance(it.code(), it.errorBody()))
                }
            }
    }

    private fun getContractAccountBalances(
        contractName: String,
        accountName: String,
        contractBalances: List<String>,
        eosPrice: EosPrice
    ): List<ContractAccountBalance> {
        return contractBalances.map {
            ContractAccountBalance(
                contractName,
                accountName,
                BalanceFormatter.deserialize(it),
                eosPrice)
        }
    }
}