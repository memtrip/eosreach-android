package com.memtrip.eosreach.api.accountforkey

import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result
import io.reactivex.Single
import okhttp3.ResponseBody

interface AccountForPublicKeyRequest {

    fun getAccountsForKey(publicKey: String): Single<Result<AccountsForPublicKey, AccountForKeyError>>
}

sealed class AccountForKeyError : ApiError {
    object Generic : AccountForKeyError()
    object InvalidPrivateKey : AccountForKeyError()
    object PrivateKeyAlreadyImported : AccountForKeyError()
    object NoAccounts : AccountForKeyError()
    data class FailedRetrievingAccountList(
        val code: Int,
        val body: ResponseBody?
    ) : AccountForKeyError()
}