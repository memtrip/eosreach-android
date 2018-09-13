package com.memtrip.eosreach.db.transaction

import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class GetTransactionLogs @Inject internal constructor(
    private val transactionLogDao: TransactionLogDao,
    private val rxSchedulers: RxSchedulers
) {

    fun select(): Single<List<TransactionLogEntity>> {
        return Single.fromCallable { transactionLogDao.getTransactions() }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }
}
