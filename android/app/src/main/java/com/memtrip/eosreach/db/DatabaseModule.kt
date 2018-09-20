package com.memtrip.eosreach.db

import android.app.Application
import androidx.room.Room
import com.memtrip.eosreach.db.account.AccountDao
import com.memtrip.eosreach.db.blockproducer.BlockProducerDao
import com.memtrip.eosreach.db.airdrop.BalanceDao
import com.memtrip.eosreach.db.transaction.TransactionLogDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun appDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application.applicationContext, AppDatabase::class.java, "eosreach")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun accountDao(appDatabase: AppDatabase): AccountDao = appDatabase.accountDao()

    @Provides
    @Singleton
    fun balanceDao(appDatabase: AppDatabase): BalanceDao = appDatabase.balanceDao()

    @Provides
    @Singleton
    fun blockProducerDao(appDatabase: AppDatabase): BlockProducerDao = appDatabase.blockProducerDao()

    @Provides
    @Singleton
    fun transactionLogDao(appDatabase: AppDatabase): TransactionLogDao = appDatabase.transactionLogDao()
}