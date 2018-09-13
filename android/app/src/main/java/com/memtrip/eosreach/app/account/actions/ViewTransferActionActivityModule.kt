package com.memtrip.eosreach.app.account.actions

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class ViewTransferActionActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(ViewTransferActionViewModel::class)
    internal abstract fun contributesViewTransferActionViewModel(viewModel: ViewTransferActionViewModel): ViewModel
}