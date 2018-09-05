package com.memtrip.eosreach.api.blockproducer

import com.memtrip.eos.http.aggregation.producer.BlockProducer
import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result

import io.reactivex.Single

interface BlockProducerRequest {

    fun getBlockProducers(limit: Int): Single<Result<List<BlockProducer>, BlockProducerError>>
}

class BlockProducerError : ApiError