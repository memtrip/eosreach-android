/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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