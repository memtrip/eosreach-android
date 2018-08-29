package com.memtrip.eosreach.api.balance

import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.model.contract.request.GetCurrencyBalance
import com.memtrip.eosreach.api.Result

import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class AccountBalanceRequestImp @Inject internal constructor(
    private val chainApi: ChainApi,
    private val rxSchedulers: RxSchedulers
) : AccountBalanceRequest {

    override fun getBalance(
        contractName: String,
        accountName: String
    ): Single<Result<AccountBalances, BalanceError>> {

        return chainApi.getCurrencyBalance(
            GetCurrencyBalance(
                contractName,
                accountName,
                "SYS"))
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.main())
            .map {
                if (it.isSuccessful) {
                    Result(AccountBalances(accountBalancesFromStrings(it.body()!!)))
                } else {
                    Result<AccountBalances, BalanceError>(
                        BalanceError.FailedRetrievingBalance(it.code(), it.errorBody()))
                }
            }
    }

    private fun accountBalancesFromStrings(balances: List<String>): List<Balance> {
        return balances.map {
            balanceFromString(it)
        }
    }

    private fun balanceFromString(balance: String): Balance {
        val parts = balance.split(" ")
        return Balance(parts[0], parts[1])
    }
}