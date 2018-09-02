package com.memtrip.eosreach.api.account

import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.model.account.request.AccountName

import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class EosAccountRequestImpl @Inject internal constructor(
    private val chainApi: ChainApi,
    private val rxSchedulers: RxSchedulers
) : EosAccountRequest {

    override fun getAccount(accountName: String): Single<Result<EosAccount, AccountError>> {

        return chainApi.getAccount(AccountName(accountName))
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.main())
            .map {
                if (it.isSuccessful) {
                    val account = it.body()!!
                    Result(
                        EosAccount(
                            account.account_name,
                            EosAccountResource(
                                account.net_limit.used,
                                account.net_limit.available),
                            EosAccountResource(
                                account.cpu_limit.used,
                                account.cpu_limit.available),
                            EosAccountResource(
                                account.ram_usage,
                                account.ram_quota)
                        )
                    )
                } else {
                    Result<EosAccount, AccountError>(
                        AccountError.FailedRetrievingAccount(it.code(), it.errorBody()))
                }
        }
    }
}