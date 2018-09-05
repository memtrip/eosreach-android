package com.memtrip.eosreach.app.blockproducerlist

import com.memtrip.eosreach.api.blockproducer.BlockProducerRequest
import com.memtrip.eosreach.db.blockproducer.BlockProducerEntity
import com.memtrip.eosreach.db.blockproducer.CountBlockProducers
import com.memtrip.eosreach.db.blockproducer.GetBlockProducers
import com.memtrip.eosreach.db.blockproducer.InsertBlockProducers
import io.reactivex.Single
import javax.inject.Inject

class BlockProducerListUseCase @Inject internal constructor(
    private val countBlockProducers: CountBlockProducers,
    private val getBlockProducers: GetBlockProducers,
    private val blockProducerRequest: BlockProducerRequest,
    private val insertBlockProducers: InsertBlockProducers

) {

    fun getBlockProducers(): Single<List<BlockProducerEntity>> {
        return countBlockProducers.count().flatMap { count ->
            if (count > 0) {
                getBlockProducers.select()
            } else {
                blockProducerRequest.getBlockProducers(21).flatMap { result ->
                    if (result.success) {
                        insertBlockProducers.replace(result.data!!)
                    } else {
                        Single.just(emptyList())
                    }
                }
            }
        }
    }
}