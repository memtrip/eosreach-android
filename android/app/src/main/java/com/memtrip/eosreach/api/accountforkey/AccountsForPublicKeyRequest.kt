package com.memtrip.eosreach.api.accountforkey

import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result
import io.reactivex.Single

interface AccountForPublicKeyRequest {

    fun getAccountsForKey(publicKey: String): Single<Result<AccountsForPublicKey, AccountForKeyError>>
}

sealed class AccountForKeyError : ApiError {
    object InvalidPrivateKey : AccountForKeyError()
    object PrivateKeyAlreadyImported : AccountForKeyError()
    object NoAccounts : AccountForKeyError()
    object FailedRetrievingAccountList : AccountForKeyError()
}