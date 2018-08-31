package com.memtrip.eosreach.db.account

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Account")
data class AccountEntity(
    @ColumnInfo(name = "publicKey") val publicKey: String,
    @ColumnInfo(name = "accountName") val accountName: String,
    @ColumnInfo(name = "balance") val balance: Double? = null,
    @ColumnInfo(name = "symbol") val symbol: String? = null,
    @PrimaryKey(autoGenerate = true) val uid: Int = 0
)
