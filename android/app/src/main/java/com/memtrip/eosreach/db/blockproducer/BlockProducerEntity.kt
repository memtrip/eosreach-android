package com.memtrip.eosreach.db.blockproducer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BlockProducer")
data class BlockProducerEntity(
    @ColumnInfo(name = "accountName") val accountName: String,
    @ColumnInfo(name = "candidateName") val candidateName: String,
    @ColumnInfo(name = "apiUrl") val apiUrl: String,
    @ColumnInfo(name = "logoUrl") val logoUrl: String? = null,
    @PrimaryKey(autoGenerate = true) val uid: Int = 0
)
