package com.memtrip.eosreach.db.contract

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Balance")
data class BalanceEntity(
    @ColumnInfo(name = "accountName") val accountName: String,
    @ColumnInfo(name = "contractName") val contractName: String,
    @ColumnInfo(name = "symbol") val symbol: String,
    @PrimaryKey(autoGenerate = true) val uid: Int = 0
)
