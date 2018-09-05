package com.memtrip.eosreach.api.blockproducer

import com.memtrip.eos.http.aggregation.producer.BlockProducer
import com.memtrip.eos.http.aggregation.producer.GetBlockProducersAggregate
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.utils.RxSchedulers

import io.reactivex.Single
import javax.inject.Inject

class BlockProducerRequestImpl @Inject internal constructor(
    private val blockProducersAggregate: GetBlockProducersAggregate,
    private val rxSchedulers: RxSchedulers
) : BlockProducerRequest {

    override fun getBlockProducers(limit: Int): Single<Result<List<BlockProducer>, BlockProducerError>> {
        return blockProducersAggregate.getProducers(limit)
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
            .map {
                Result<List<BlockProducer>, BlockProducerError>(it)
            }
            .onErrorReturn {
                Result(BlockProducerError())
            }
    }
}