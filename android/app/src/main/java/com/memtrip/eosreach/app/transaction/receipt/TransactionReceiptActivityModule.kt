package com.memtrip.eosreach.app.transaction.receipt

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class TransactionReceiptActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(TransactionReceiptViewModel::class)
    internal abstract fun contributesTransferReceiptViewModel(viewModel: TransactionReceiptViewModel): ViewModel
}