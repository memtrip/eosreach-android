package com.memtrip.eosreach.api.balance

import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.model.contract.request.GetCurrencyBalance
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.app.price.BalanceParser

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
        symbol: String
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
                        it.body()!!)
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
        contractBalances: List<String>
    ): List<ContractAccountBalance> {
        return contractBalances.map {
            ContractAccountBalance(contractName, accountName, BalanceParser.deserialize(it))
        }
    }
}