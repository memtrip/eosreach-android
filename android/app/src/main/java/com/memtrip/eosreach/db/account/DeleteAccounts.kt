package com.memtrip.eosreach.db.account

import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Completable

import javax.inject.Inject

class DeleteAccounts @Inject internal constructor(
    private val accountDao: AccountDao,
    private val rxSchedulers: RxSchedulers
) {

    fun remove(): Completable {
        return Completable.fromAction { accountDao.delete() }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }
}