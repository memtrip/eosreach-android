package com.memtrip.eosreach.app

import android.app.Application
import com.memtrip.eosreach.api.ApiConfig
import com.memtrip.eosreach.api.ApiModule
import com.memtrip.eosreach.api.RequestModule
import com.memtrip.eosreach.app.account.AccountModule

import com.memtrip.eosreach.app.accountlist.AccountListModule
import com.memtrip.eosreach.app.manage.ManageModule
import com.memtrip.eosreach.app.settings.SettingsModule
import com.memtrip.eosreach.app.welcome.WelcomeModule
import com.memtrip.eosreach.db.DatabaseModule

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
        WelcomeModule::class,
        AccountModule::class,
        ManageModule::class,
        SettingsModule::class,
        AccountListModule::class,
        DatabaseModule::class,
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

        @BindsInstance
        fun apiConfig(apiConfig: ApiConfig): Builder

        fun build(): EosReachApplicationComponent
    }
}