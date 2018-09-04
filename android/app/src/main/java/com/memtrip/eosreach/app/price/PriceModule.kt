package com.memtrip.eosreach.app.price

import com.memtrip.eosreach.app.price.currencypairing.CurrencyPairingActivity
import com.memtrip.eosreach.app.price.currencypairing.CurrencyPairingActivityModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PriceModule {

    @ContributesAndroidInjector(modules = [CurrencyPairingActivityModule::class])
    internal abstract fun contributeCurrencyPairingActivity(): CurrencyPairingActivity
}