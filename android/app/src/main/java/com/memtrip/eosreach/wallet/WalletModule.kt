package com.memtrip.eosreach.wallet

import android.app.Application

import android.os.Build

import dagger.Module
import dagger.Provides

@Module
internal object WalletModule {

    @JvmStatic
    @Provides
    fun providesWallet(application: Application): EosKeyManager {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            return EosKeyManagerApi21(application)
        } else {
            // TODO: API23+ wallet (AES)
            return EosKeyManagerApi21(application)
        }
    }
}