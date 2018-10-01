/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.memtrip.eosreach.app

import android.app.Application
import com.memtrip.eosreach.api.ApiConfig
import com.memtrip.eosreach.api.ApiModule
import com.memtrip.eosreach.api.RequestModule
import com.memtrip.eosreach.app.account.AccountModule
import com.memtrip.eosreach.app.account.actions.ActionsModule
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthModule
import com.memtrip.eosreach.app.account.resources.manage.manageram.ManageRamModule
import com.memtrip.eosreach.app.account.vote.cast.CastModule
import com.memtrip.eosreach.app.accountlist.AccountListModule
import com.memtrip.eosreach.app.blockproducerlist.BlockProducerListModule
import com.memtrip.eosreach.app.manage.ManageModule
import com.memtrip.eosreach.app.price.PriceModule
import com.memtrip.eosreach.app.settings.SettingsModule
import com.memtrip.eosreach.app.transaction.TransactionModule
import com.memtrip.eosreach.app.transfer.TransferModule
import com.memtrip.eosreach.app.welcome.WelcomeModule
import com.memtrip.eosreach.billing.BillingConfig
import com.memtrip.eosreach.billing.BillingModule
import com.memtrip.eosreach.db.DatabaseModule
import com.memtrip.eosreach.utils.UtilModule
import com.memtrip.eosreach.wallet.SecurityModule
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
        CastModule::class,
        BandwidthModule::class,
        ManageRamModule::class,
        ActionsModule::class,
        ManageModule::class,
        TransferModule::class,
        TransactionModule::class,
        PriceModule::class,
        SettingsModule::class,
        AccountListModule::class,
        BlockProducerListModule::class,
        BillingModule::class,
        DatabaseModule::class,
        SecurityModule::class,
        ApiModule::class,
        RequestModule::class,
        UtilModule::class
    ]
)
interface EosReachApplicationComponent : AndroidInjector<BaseEosReachApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun apiConfig(apiConfig: ApiConfig): Builder

        @BindsInstance
        fun billingConfig(billingConfig: BillingConfig?): Builder

        fun build(): EosReachApplicationComponent
    }
}