package com.memtrip.eosreach.db

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
internal object StorageModule {

    @JvmStatic
    @Provides
    fun providesEosReachSharedPreferences(application: Application): EosReachSharedPreferences {
        return EosReachSharedPreferences(application)
    }
}