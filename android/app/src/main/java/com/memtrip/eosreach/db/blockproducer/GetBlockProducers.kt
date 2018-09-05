package com.memtrip.eosreach.db.blockproducer

import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class GetBlockProducers @Inject internal constructor(
    private val blockProducerDao: BlockProducerDao,
    private val rxSchedulers: RxSchedulers
) {

    fun select(): Single<List<BlockProducerEntity>> {
        return Single.fromCallable { blockProducerDao.getBlockProducers() }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }
}
