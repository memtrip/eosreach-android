package com.memtrip.eosreach.db.transaction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TransactionLog")
data class TransactionLogEntity(
    @ColumnInfo(name = "transactionId") val transactionId: String,
    @ColumnInfo(name = "formattedDate") val formattedDate: String,
    @PrimaryKey(autoGenerate = true) val uid: Int = 0
)
