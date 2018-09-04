package com.memtrip.eosreach.app.transfer.receipt

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class TransferReceiptActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(TransferReceiptViewModel::class)
    internal abstract fun contributesTransferReceiptViewModel(viewModel: TransferReceiptViewModel): ViewModel
}