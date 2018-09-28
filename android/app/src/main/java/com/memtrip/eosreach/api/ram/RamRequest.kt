package com.memtrip.eosreach.api.ram

import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.transfer.ActionReceipt
import io.reactivex.Single

interface RamRequest {

    fun buy(
        receiver: String,
        kb: Double,
        authorizingPrivateKey: EosPrivateKey
    ): Single<Result<ActionReceipt, RamError>>

    fun sell(
        account: String,
        kb: Double,
        authorizingPrivateKey: EosPrivateKey
    ): Single<Result<ActionReceipt, RamError>>
}

class RamError(val body: String) : ApiError