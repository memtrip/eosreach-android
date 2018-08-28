package com.memtrip.eosreach.api

import dagger.Binds
import dagger.Module

@Module
internal abstract class AccountHistoryModule {

    @Binds
    internal abstract fun bindAccountHistoryManager(accountHistoryManagerImpl: AccountHistoryManagerImpl): AccountHistoryManager
}
