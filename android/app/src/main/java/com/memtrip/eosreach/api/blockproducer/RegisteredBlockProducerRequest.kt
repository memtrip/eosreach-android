package com.memtrip.eosreach.api.blockproducer

import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result
import io.reactivex.Single

interface RegisteredBlockProducerRequest {
    fun getProducers(limit: Int, lowerLimit: String): Single<Result<List<RegisteredBlockProducer>, RegisteredBlockProducerError>>
}

sealed class RegisteredBlockProducerError : ApiError {
    object Empty : RegisteredBlockProducerError()
    object GenericError : RegisteredBlockProducerError()
}