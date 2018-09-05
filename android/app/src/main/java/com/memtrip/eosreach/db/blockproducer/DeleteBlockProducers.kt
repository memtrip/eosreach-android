package com.memtrip.eosreach.db.blockproducer

import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Completable

import javax.inject.Inject

class DeleteBlockProducers @Inject internal constructor(
    private val blockProducerDao: BlockProducerDao,
    private val rxSchedulers: RxSchedulers
) {

    fun remove(): Completable {
        return Completable.fromAction { blockProducerDao.delete() }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }
}