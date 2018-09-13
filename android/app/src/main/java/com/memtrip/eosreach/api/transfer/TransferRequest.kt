package com.memtrip.eosreach.api.transfer

import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eos.http.rpc.model.transaction.response.TransactionCommitted
import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result
import io.reactivex.Single

interface TransferRequest {

    fun transfer(
        fromAccount: String,
        toAccount: String,
        quantity: String,
        memo: String,
        authorizingPrivateKey: EosPrivateKey
    ): Single<Result<TransactionCommitted, TransferError>>
}

class TransferError(val body: String) : ApiError