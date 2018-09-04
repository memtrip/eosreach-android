package com.memtrip.eosreach.app.price.currencypairing

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class CurrencyPairingActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(CurrencyPairingViewModel::class)
    internal abstract fun contributesCurrencyPairingViewModel(viewModel: CurrencyPairingViewModel): ViewModel
}