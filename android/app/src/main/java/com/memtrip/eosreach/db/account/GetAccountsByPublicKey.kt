package com.memtrip.eosreach.db.account

import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class GetAccountNamesForPublicKey @Inject internal constructor(
    private val accountDao: AccountDao,
    private val rxSchedulers: RxSchedulers
) {

    fun names(publicKey: String): Single<List<String>> {
        return Single.fromCallable { accountDao.getAccountsByPublicKey(publicKey) }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
            .map { accountEntities ->
                accountEntities.map { accountEntity ->
                    accountEntity.accountName
                }
            }
    }
}