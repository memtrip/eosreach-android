package com.memtrip.eosreach.db.transaction

import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Completable
import javax.inject.Inject

class InsertTransactionLog @Inject internal constructor(
    private val transactionLogDao: TransactionLogDao,
    private val rxSchedulers: RxSchedulers
) {

    fun insert(transactionLogEntity: TransactionLogEntity): Completable {
        return Completable
            .fromAction { transactionLogDao.insert(transactionLogEntity) }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }
}