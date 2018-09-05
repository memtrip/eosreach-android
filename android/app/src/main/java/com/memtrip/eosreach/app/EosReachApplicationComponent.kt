package com.memtrip.eosreach.app

import android.app.Application
import com.memtrip.eosreach.api.ApiConfig
import com.memtrip.eosreach.api.ApiModule
import com.memtrip.eosreach.api.RequestModule
import com.memtrip.eosreach.app.account.AccountModule

import com.memtrip.eosreach.app.accountlist.AccountListModule
import com.memtrip.eosreach.app.blockproducerlist.BlockProducerListModule
import com.memtrip.eosreach.app.manage.ManageModule
import com.memtrip.eosreach.app.price.PriceModule
import com.memtrip.eosreach.app.settings.SettingsModule
import com.memtrip.eosreach.app.transfer.TransferModule
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
        TransferModule::class,
        PriceModule::class,
        SettingsModule::class,
        AccountListModule::class,
        BlockProducerListModule::class,
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