package com.memtrip.eosreach.db.transaction

import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Completable
import javax.inject.Inject

class DeleteTransactionLog @Inject internal constructor(
    private val transactionLogDao: TransactionLogDao,
    private val rxSchedulers: RxSchedulers
) {

    fun remove(): Completable {
        return Completable
            .fromAction { transactionLogDao.delete() }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }
}