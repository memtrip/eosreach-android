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