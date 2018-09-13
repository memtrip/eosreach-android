package com.memtrip.eosreach.app.transaction.log

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class ViewConfirmedTransactionsActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(ViewConfirmedTransactionsViewModel::class)
    internal abstract fun contributesViewConfirmedTransactionsViewModel(viewModel: ViewConfirmedTransactionsViewModel): ViewModel
}