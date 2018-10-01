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
package com.memtrip.eosreach.api.accountforkey

import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.HistoryApi
import com.memtrip.eos.http.rpc.model.contract.request.GetCurrencyBalance
import com.memtrip.eos.http.rpc.model.history.request.GetKeyAccounts
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import retrofit2.Response
import javax.inject.Inject

internal class AccountsForPublicKeyRequestImpl @Inject internal constructor(
    private val historyApi: HistoryApi,
    private val chainApi: ChainApi,
    private val rxSchedulers: RxSchedulers
) : AccountForPublicKeyRequest {

    override fun getAccountsForKey(publicKey: String): Single<Result<AccountsForPublicKey, AccountForKeyError>> {
        return historyApi.getKeyAccounts(GetKeyAccounts(publicKey))
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.main())
            .flatMap {
                if (it.isSuccessful) {
                    getBalance(publicKey, it.body()!!.account_names)
                } else {
                    Single.just(Result<AccountsForPublicKey, AccountForKeyError>(
                        AccountForKeyError.FailedRetrievingAccountList))
                }
            }
    }

    private fun getBalance(publicKey: String, accountNameList: List<String>): Single<Result<AccountsForPublicKey, AccountForKeyError>> {

        return Observable.fromIterable(accountNameList)
            .concatMap { accountName ->
                Observable.zip(
                    Observable.just(accountName),
                    chainApi.getCurrencyBalance(GetCurrencyBalance(
                        "eosio.token",
                        accountName
                    )).toObservable(),
                    BiFunction<String, Response<List<String>>, AccountNameSystemBalance> { _, response ->
                        if (response.isSuccessful) {
                            val balance = response.body()!!
                            if (balance.isNotEmpty()) {
                                AccountNameSystemBalance(accountName, balance[0])
                            } else {
                                AccountNameSystemBalance(accountName)
                            }
                        } else {
                            throw InnerAccountFailed()
                        }
                    })
            }
            .toList()
            .map { accountSystemBalanceList ->
                Result<AccountsForPublicKey, AccountForKeyError>(AccountsForPublicKey(publicKey, accountSystemBalanceList))
            }
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.main())
    }

    class InnerAccountFailed : Exception()
}