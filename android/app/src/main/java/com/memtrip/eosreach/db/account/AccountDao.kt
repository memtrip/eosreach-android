package com.memtrip.eosreach.db.account

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AccountDao {

    @Insert
    fun insertAll(pinyin: List<AccountEntity>)

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