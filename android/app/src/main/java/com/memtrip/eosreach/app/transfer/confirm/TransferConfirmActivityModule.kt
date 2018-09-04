package com.memtrip.eosreach.app.transfer.confirm

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class TransferConfirmActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(TransferConfirmViewModel::class)
    internal abstract fun contributesTransferConfirmViewModel(viewModel: TransferConfirmViewModel): ViewModel
}