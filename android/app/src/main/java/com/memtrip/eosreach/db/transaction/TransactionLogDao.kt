package com.memtrip.eosreach.db.transaction

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionLogDao {

    @Insert
    fun insert(transaction: TransactionLogEntity)

    @Query("SELECT * FROM TransactionLog ORDER BY uid ASC LIMIT 0, 100")
    fun getTransactions(): List<TransactionLogEntity>

    @Query("DELETE FROM TransactionLog")
    fun delete()
}