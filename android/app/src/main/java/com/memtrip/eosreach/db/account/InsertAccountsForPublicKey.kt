package com.memtrip.eosreach.db.account

import com.memtrip.eosreach.api.accountforkey.AccountNameSystemBalance
import com.memtrip.eosreach.app.price.BalanceFormatter
import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class InsertAccountsForPublicKey @Inject internal constructor(
    private val accountDao: AccountDao,
    private val rxSchedulers: RxSchedulers
) {

    fun replace(publicKey: String, accounts: List<AccountNameSystemBalance>): Single<List<AccountEntity>> {

        val publicKeyAccountEntities = accounts.map { accountNameSystemBalance ->
            if (accountNameSystemBalance.systemBalance != null) {
                val balance = BalanceFormatter.deserialize(accountNameSystemBalance.systemBalance)
                AccountEntity(
                    publicKey,
                    accountNameSystemBalance.accountName,
                    balance.amount,
                    balance.symbol)
            } else {
                AccountEntity(
                    publicKey,
                    accountNameSystemBalance.accountName)
            }
        }

        return deletePublicKey(publicKey)
            .andThen(insertPublicKeys(publicKeyAccountEntities))
            .toSingle { publicKeyAccountEntities }
    }
    private fun deletePublicKey(publicKey: String): Completable {
        return Completable
            .fromAction { accountDao.deleteBy(publicKey) }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }

    private fun insertPublicKeys(publicKeyAccountEntities: List<AccountEntity>): Completable {
        return Completable.fromAction { accountDao.insertAll(publicKeyAccountEntities) }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }

}