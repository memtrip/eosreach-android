package com.memtrip.eosreach.wallet

import android.app.Application
import com.memtrip.eosreach.utils.RxSchedulers
import dagger.Module
import dagger.Provides

@Module
internal object SecurityModule {

    @JvmStatic
    @Provides
    fun keyStoreWrapper(application: Application): KeyStoreWrapper = KeyStoreWrapper(application)

    @JvmStatic
    @Provides
    fun cipherWrapper(): CipherWrapper = CipherWrapper()

    @JvmStatic
    @Provides
    fun providesEosKeyManager(
        keyStoreWrapper: KeyStoreWrapper,
        cipherWrapper: CipherWrapper,
        rxSchedulers: RxSchedulers,
        application: Application
    ): EosKeyManager = EosKeyManagerImpl(
        keyStoreWrapper,
        cipherWrapper,
        rxSchedulers,
        application
    )
}