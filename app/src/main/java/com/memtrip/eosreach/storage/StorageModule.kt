package com.memtrip.eosreach.storage

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
internal object StorageModule {

    @JvmStatic
    @Provides
    fun getEosReachSharedPreferences(application: Application): EosReachSharedPreferences {
        return EosReachSharedPreferences(application)
    }
}