package com.memtrip.eosreach.api.balance

import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.model.contract.request.GetCurrencyBalance
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.utils.BalanceParser

import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class AccountBalanceRequestImp @Inject internal constructor(
    private val chainApi: ChainApi,
    private val rxSchedulers: RxSchedulers,
    private val balanceParser: BalanceParser
) : AccountBalanceRequest {

    override fun getBalance(
        contractName: String,
        accountName: String
    ): Single<Result<AccountBalanceList, BalanceError>> {

        return chainApi.getCurrencyBalance(
            GetCurrencyBalance(
                contractName,
                accountName,
                "SYS"))
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.main())
            .map {
                if (it.isSuccessful) {
                    Result(AccountBalanceList(accountBalancesFromStrings(accountName, it.body()!!)))
                } else {
                    Result<AccountBalanceList, BalanceError>(
                        BalanceError.FailedRetrievingBalance(it.code(), it.errorBody()))
                }
            }
    }

    private fun accountBalancesFromStrings(accountName: String, balances: List<String>): List<AccountBalance> {
        return balances.map {
            AccountBalance(accountName, balanceParser.pull(it))
        }
    }
}