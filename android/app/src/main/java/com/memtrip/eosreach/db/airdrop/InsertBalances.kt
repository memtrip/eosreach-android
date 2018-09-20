package com.memtrip.eosreach.db.airdrop

import com.memtrip.eosreach.api.balance.ContractAccountBalance

import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Completable

import io.reactivex.Single
import javax.inject.Inject

class InsertBalances @Inject internal constructor(
    private val balanceDao: BalanceDao,
    private val rxSchedulers: RxSchedulers
) {

    fun insert(accountName: String, contractAccountBalances: List<ContractAccountBalance>): Single<List<BalanceEntity>> {

        val balanceEntities = contractAccountBalances.map { contractAccountBalance ->
            BalanceEntity(
                contractAccountBalance.accountName,
                contractAccountBalance.contractName,
                contractAccountBalance.balance.symbol
            )
        }

        return deleteByAccountName(accountName)
            .andThen(insertBalances(balanceEntities))
            .toSingle { balanceEntities }
    }


    private fun deleteByAccountName(accountName: String): Completable {
        return Completable
            .fromAction { balanceDao.deleteBy(accountName) }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }

    private fun insertBalances(balanceEntities: List<BalanceEntity>): Completable {
        return Completable.fromAction { balanceDao.insertAll(balanceEntities) }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }
}