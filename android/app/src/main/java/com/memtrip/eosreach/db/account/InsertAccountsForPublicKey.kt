package com.memtrip.eosreach.db.account

import com.memtrip.eosreach.api.accountforkey.AccountNameSystemBalance
import com.memtrip.eosreach.utils.BalanceParser
import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class InsertAccountsForPublicKey @Inject internal constructor(
    private val accountDao: AccountDao,
    private val rxSchedulers: RxSchedulers,
    private val balanceParser: BalanceParser
) {

    fun replace(publicKey: String, accounts: List<AccountNameSystemBalance>): Single<List<AccountEntity>> {

        val publicKeyAccountEntities = accounts.map { accountNameSystemBalance ->
            if (accountNameSystemBalance.systemBalance != null) {
                val balance = balanceParser.pull(accountNameSystemBalance.systemBalance)
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

        return Completable
            .fromAction { accountDao.deleteBy(publicKey) }
            .andThen(Completable.fromAction { accountDao.insertAll(publicKeyAccountEntities) })
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
            .toSingle { publicKeyAccountEntities }
    }
}