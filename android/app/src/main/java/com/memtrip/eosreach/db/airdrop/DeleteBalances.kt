package com.memtrip.eosreach.db.airdrop

import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Completable

import javax.inject.Inject

class DeleteBalances @Inject internal constructor(
    private val balanceDao: BalanceDao,
    private val rxSchedulers: RxSchedulers
) {

    fun remove(): Completable {
        return Completable.fromAction { balanceDao.delete() }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }
}