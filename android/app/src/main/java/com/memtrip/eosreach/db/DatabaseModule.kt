/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.memtrip.eosreach.db

import android.app.Application
import androidx.room.Room
import com.memtrip.eosreach.R
import com.memtrip.eosreach.db.AppDatabase.Companion.MIGRATION_1_2
import com.memtrip.eosreach.db.account.AccountDao
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
        return Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            application.getString(R.string.app_database_name)
        ).addMigrations(MIGRATION_1_2).build()
    }

    @Provides
    @Singleton
    fun accountDao(appDatabase: AppDatabase): AccountDao = appDatabase.accountDao()

    @Provides
    @Singleton
    fun balanceDao(appDatabase: AppDatabase): BalanceDao = appDatabase.balanceDao()

    @Provides
    @Singleton
    fun transactionLogDao(appDatabase: AppDatabase): TransactionLogDao = appDatabase.transactionLogDao()
}