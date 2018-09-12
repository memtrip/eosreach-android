package com.memtrip.eosreach.api.eoscreateaccount

import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result
import io.reactivex.Single

interface EosCreateAccountRequest {

    fun createAccount(
        purchaseToken: String,
        accountName: String,
        publicKey: String
    ) : Single<Result<CreateAccountReceipt, EosCreateAccountError>>
}

sealed class EosCreateAccountError : ApiError {
    object GenericError : EosCreateAccountError()
    object FatalError : EosCreateAccountError()
    object AccountNameExists : EosCreateAccountError()
}