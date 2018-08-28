package com.memtrip.eosreach.wallet

import android.content.Context
import android.os.Build

import dagger.Module
import dagger.Provides

@Module
internal object WalletModule {

    @JvmStatic
    @Provides
    fun providesWallet(context: Context): Wallet {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            return WalletApi21(context)
        } else {
            // TODO: API23+ wallet (AES)
            return WalletApi21(context)
        }
    }
}