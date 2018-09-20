package com.memtrip.eosreach.db.airdrop

import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class GetBalances @Inject internal constructor(
    private val balancesDao: BalanceDao,
    private val rxSchedulers: RxSchedulers
) {

    fun select(accountName: String): Single<List<BalanceEntity>> {
        return Single.fromCallable { balancesDao.getBalancesForAccount(accountName) }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }
}