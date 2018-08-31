package com.memtrip.eosreach.db

import androidx.room.Database

import androidx.room.RoomDatabase
import com.memtrip.eosreach.db.account.AccountDao
import com.memtrip.eosreach.db.account.AccountEntity

@Database(entities = [AccountEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
}