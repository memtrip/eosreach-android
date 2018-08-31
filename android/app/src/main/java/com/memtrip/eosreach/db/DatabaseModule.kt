package com.memtrip.eosreach.db

import android.app.Application
import androidx.room.Room
import com.memtrip.eosreach.db.account.AccountDao
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
}