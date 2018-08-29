package com.memtrip.eosreach.app

import android.app.Application
import com.memtrip.eosreach.api.ApiModule
import com.memtrip.eosreach.api.RequestModule
import com.memtrip.eosreach.app.account.AccountActivityModule
import com.memtrip.eosreach.app.welcome.WelcomeNavigationActivityModule
import com.memtrip.eosreach.db.DatabaseModule
import com.memtrip.eosreach.db.StorageModule
import com.memtrip.eosreach.utils.UtilModule
import com.memtrip.eosreach.wallet.WalletModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        WelcomeNavigationActivityModule::class,
        AccountActivityModule::class,
        DatabaseModule::class,
        StorageModule::class,
        WalletModule::class,
        ApiModule::class,
        RequestModule::class,
        UtilModule::class
    ]
)
interface EosReachApplicationComponent : AndroidInjector<EosReachApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): EosReachApplicationComponent
    }
}