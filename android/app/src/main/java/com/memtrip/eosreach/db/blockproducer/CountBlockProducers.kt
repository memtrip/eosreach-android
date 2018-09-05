package com.memtrip.eosreach.db.blockproducer

import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class CountBlockProducers @Inject internal constructor(
    private val blockProducerDao: BlockProducerDao,
    private val rxSchedulers: RxSchedulers
) {

    fun count(): Single<Int> {
        return Single.fromCallable { blockProducerDao.count() }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }
}