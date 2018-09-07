package com.memtrip.eosreach.app.transaction.log

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class TransactionLogActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(TransactionLogViewModel::class)
    internal abstract fun contributesTransactionLogViewModel(viewModel: TransactionLogViewModel): ViewModel
}