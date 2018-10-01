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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AccountDao {

    @Insert
    fun insertAll(accounts: List<AccountEntity>)

    @Query("DELETE FROM Account WHERE publicKey = :publicKey")
    fun deleteBy(publicKey: String)

    @Query("SELECT DISTINCT uid, publicKey, accountName, balance, symbol FROM Account WHERE publicKey = :publicKey ORDER BY uid ASC LIMIT 0, 100")
    fun getAccountsForPublicKey(publicKey: String): List<AccountEntity>

    @Query("SELECT DISTINCT uid, publicKey, accountName, balance, symbol FROM Account ORDER BY uid ASC LIMIT 0, 100")
    fun getAccounts(): List<AccountEntity>

    @Query("SELECT * FROM Account WHERE accountName = :accountName ORDER BY uid ASC LIMIT 0, 1")
    fun getAccountByName(accountName: String): List<AccountEntity>

    @Query("SELECT COUNT(*) FROM Account")
    fun count(): Int

    @Query("DELETE FROM Account")
    fun delete()
}