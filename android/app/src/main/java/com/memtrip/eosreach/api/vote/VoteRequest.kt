package com.memtrip.eosreach.api.vote

import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result

import io.reactivex.Single

interface VoteRequest {

    fun voteForProducer(
        voterAccountName: String,
        producers: List<String>
    ): Single<Result<VoteReceipt, VoteError>>

    fun voteForProxy(
        voterAccountName: String,
        proxyVoteAccountName: String
    ): Single<Result<VoteReceipt, VoteError>>
}

class VoteError(val body: String) : ApiError