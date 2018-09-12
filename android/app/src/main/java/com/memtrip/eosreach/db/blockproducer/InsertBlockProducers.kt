package com.memtrip.eosreach.db.blockproducer

import com.memtrip.eos.chain.actions.query.producer.BlockProducer

import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class InsertBlockProducers @Inject internal constructor(
    private val blockProducerDao: BlockProducerDao,
    private val rxSchedulers: RxSchedulers
) {

    fun replace(blockProducers: List<BlockProducer>): Single<List<BlockProducerEntity>> {

        val blockProducerEntities = blockProducers.map { blockProducer ->
            BlockProducerEntity(
                blockProducer.producer.owner,
                blockProducer.bpJson.org.candidate_name,
                blockProducer.apiEndpoint,
                blockProducer.bpJson.org.branding.logo_256
            )
        }

        return deleteBlockProducers()
            .andThen(insertBlockProducers(blockProducerEntities))
            .toSingle { blockProducerEntities }
    }

    private fun deleteBlockProducers(): Completable {
        return Completable
            .fromAction { blockProducerDao.delete() }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }

    private fun insertBlockProducers(blockProducerEntities: List<BlockProducerEntity>): Completable {
        return Completable
            .fromAction { blockProducerDao.insertAll(blockProducerEntities) }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }
}