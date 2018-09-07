package com.memtrip.eosreach.api.vote

import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result

import io.reactivex.Single

interface VoteRequest {

    fun vote(
        voter: String,
        producers: List<String>
    ): Single<Result<VoteReceipt, VoteError>>
}

class VoteError(val body: String) : ApiError