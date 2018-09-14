package com.memtrip.eosreach.api.bandwidth

import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eos.http.rpc.model.transaction.response.TransactionCommitted
import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.transfer.ActionReceipt
import io.reactivex.Single

interface BandwidthRequest {

    fun delegate(
        fromAccount: String,
        netAmount: String,
        cpuAmount: String,
        authorizingPrivateKey: EosPrivateKey
    ): Single<Result<ActionReceipt, BandwidthError>>

    fun unDelegate(
        fromAccount: String,
        netAmount: String,
        cpuAmount: String,
        authorizingPrivateKey: EosPrivateKey
    ): Single<Result<ActionReceipt, BandwidthError>>
}

class BandwidthError(val body: String) : ApiError