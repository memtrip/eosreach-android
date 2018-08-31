package com.memtrip.eosreach.api.accountforkey

import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.HistoryApi
import com.memtrip.eos.http.rpc.model.contract.request.GetCurrencyBalance
import com.memtrip.eos.http.rpc.model.history.request.GetKeyAccounts
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

internal class AccountsForPublicKeyRequestImpl @Inject internal constructor(
    private val historyApi: HistoryApi,
    private val chainApi: ChainApi,
    private val rxSchedulers: RxSchedulers
) : AccountForPublicKeyRequest {

    override fun getAccountsForKey(publicKey: String): Single<Result<AccountsForPublicKey, AccountForKeyError>> {
        return historyApi.getKeyAccounts(GetKeyAccounts(publicKey))
            .map {
                if (it.isSuccessful) {
                    if (it.body()!!.account_names.isEmpty()) {
                        Result<AccountsForPublicKey, AccountForKeyError>(AccountForKeyError.NoAccounts)
                    } else {
                        Result(AccountsForPublicKey(publicKey, it.body()!!.account_names.map {
                            accountName -> getBalance(accountName)
                        }))
                    }
                } else {
                    Result<AccountsForPublicKey, AccountForKeyError>(
                        AccountForKeyError.FailedRetrievingAccountList(it.code(), it.errorBody()))
                }
            }
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.main())
    }

    @Throws(InnerAccountFailed::class)
    private fun getBalance(accountName: String): AccountNameSystemBalance {
        val response = chainApi.getCurrencyBalance(GetCurrencyBalance(
            "eosio.token",
            accountName
        )).blockingGet()

        if (response.isSuccessful) {
            val balance = response.body()!!
            return if (balance.isNotEmpty()) {
                AccountNameSystemBalance(accountName, balance[0])
            } else {
                AccountNameSystemBalance(accountName)
            }
        } else {
            throw InnerAccountFailed()
        }
    }

    class InnerAccountFailed : Exception()
}