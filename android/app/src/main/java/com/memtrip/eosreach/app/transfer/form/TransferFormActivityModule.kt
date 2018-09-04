package com.memtrip.eosreach.app.transfer.form

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class TransferFormActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(TransferFormViewModel::class)
    internal abstract fun contributesTransferViewModel(viewModel: TransferFormViewModel): ViewModel
}