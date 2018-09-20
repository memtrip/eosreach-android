package com.memtrip.eosreach.db.contract

import com.memtrip.eosreach.api.balance.ContractAccountBalance

import com.memtrip.eosreach.utils.RxSchedulers

import io.reactivex.Single
import javax.inject.Inject

class InsertBalances @Inject internal constructor(
    private val balanceDao: BalanceDao,
    private val rxSchedulers: RxSchedulers
) {

    fun insert(contractAccountBalances: List<ContractAccountBalance>): Single<List<BalanceEntity>> {

        val balanceEntities = contractAccountBalances.map { contractAccountBalance ->
            BalanceEntity(
                contractAccountBalance.accountName,
                contractAccountBalance.contractName,
                contractAccountBalance.balance.symbol
            )
        }

        return Single.create<List<BalanceEntity>> {
            balanceDao.insertAll(balanceEntities)
            it.onSuccess(balanceEntities)
        }.observeOn(rxSchedulers.main()).subscribeOn(rxSchedulers.background())
    }
}