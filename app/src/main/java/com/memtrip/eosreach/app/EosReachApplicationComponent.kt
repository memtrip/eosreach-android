package com.memtrip.eosreach.app

import android.app.Application
import com.memtrip.eosreach.app.account.AccountNavigationActivityModule
import com.memtrip.eosreach.app.welcome.WelcomeNavigationActivityModule
import com.memtrip.eosreach.storage.StorageModule
import com.memtrip.eosreach.wallet.WalletModule

import dagger.BindsInstance
import dagger.Component

import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    WelcomeNavigationActivityModule::class,
    AccountNavigationActivityModule::class,
    StorageModule::class,
    WalletModule::class
])
interface EosReachApplicationComponent : AndroidInjector<EosReachApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): EosReachApplicationComponent
    }
}