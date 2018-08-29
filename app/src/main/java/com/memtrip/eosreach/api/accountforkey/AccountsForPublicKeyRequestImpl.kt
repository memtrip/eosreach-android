package com.memtrip.eosreach.api.accountforkey

import com.memtrip.eos.http.rpc.HistoryApi
import com.memtrip.eos.http.rpc.model.history.request.GetKeyAccounts

import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.utils.RxSchedulers

import io.reactivex.Single

import javax.inject.Inject

internal class AccountsForPublicKeyRequestImpl @Inject internal constructor(
    private val historyApi: HistoryApi,
    private val rxSchedulers: RxSchedulers
) : AccountForPublicKeyRequest {

    override fun getAccountsForKey(publicKey: String): Single<Result<AccountsForPublicKey, AccountForKeyError>> {
        return historyApi.getKeyAccounts(GetKeyAccounts(publicKey))
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.main())
            .map {
                if (it.isSuccessful) {
                    if (it.body()!!.account_names.isEmpty()) {
                        Result<AccountsForPublicKey, AccountForKeyError>(AccountForKeyError.NoAccounts)
                    } else {
                        Result(AccountsForPublicKey(publicKey, it.body()!!.account_names))
                    }
                } else {
                    Result<AccountsForPublicKey, AccountForKeyError>(
                        AccountForKeyError.FailedRetrievingAccountList(it.code(), it.errorBody()))
                }
            }
    }
}