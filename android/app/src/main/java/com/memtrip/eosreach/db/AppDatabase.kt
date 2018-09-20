package com.memtrip.eosreach.db

import androidx.room.Database

import androidx.room.RoomDatabase
import com.memtrip.eosreach.db.account.AccountDao
import com.memtrip.eosreach.db.account.AccountEntity
import com.memtrip.eosreach.db.blockproducer.BlockProducerDao
import com.memtrip.eosreach.db.blockproducer.BlockProducerEntity
import com.memtrip.eosreach.db.contract.BalanceDao
import com.memtrip.eosreach.db.contract.BalanceEntity
import com.memtrip.eosreach.db.transaction.TransactionLogDao
import com.memtrip.eosreach.db.transaction.TransactionLogEntity

@Database(entities = [
    AccountEntity::class,
    BalanceEntity::class,
    BlockProducerEntity::class,
    TransactionLogEntity::class
], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun balanceDao(): BalanceDao
    abstract fun blockProducerDao(): BlockProducerDao
    abstract fun transactionLogDao() : TransactionLogDao
}