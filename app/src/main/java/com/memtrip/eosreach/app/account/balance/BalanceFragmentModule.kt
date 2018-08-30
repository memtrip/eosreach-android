package com.memtrip.eosreach.app.account.balance

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class BalanceFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(BalanceViewModel::class)
    internal abstract fun contributesBalanceViewModel(viewModel: BalanceViewModel): ViewModel
}