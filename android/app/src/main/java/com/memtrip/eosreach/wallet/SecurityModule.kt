package com.memtrip.eosreach.wallet

import android.app.Application

import android.os.Build

import dagger.Module
import dagger.Provides

@Module
internal object SecurityModule {

    @JvmStatic
    @Provides
    fun providesEosKeyManager(application: Application): EosKeyManager = EosKeyManagerImpl(application)
}