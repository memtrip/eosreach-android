package com.memtrip.eosreach.db.contract

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BalanceDao {

    @Insert
    fun insertAll(balances: List<BalanceEntity>)

    @Query("SELECT DISTINCT uid, accountName, contractName, symbol FROM Balance WHERE accountName = :accountName ORDER BY uid ASC LIMIT 0, 100")
    fun getBalancesForAccount(accountName: String): List<BalanceEntity>

    @Query("DELETE FROM Balance")
    fun delete()
}