package com.memtrip.eosreach.db.blockproducer

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BlockProducerDao {

    @Insert
    fun insertAll(blockProducerList: List<BlockProducerEntity>)

    @Query("SELECT * FROM BlockProducer ORDER BY uid ASC LIMIT 0, 21")
    fun getBlockProducers(): List<BlockProducerEntity>

    @Query("SELECT COUNT(*) FROM BlockProducer")
    fun count(): Int

    @Query("DELETE FROM BlockProducer")
    fun delete()
}