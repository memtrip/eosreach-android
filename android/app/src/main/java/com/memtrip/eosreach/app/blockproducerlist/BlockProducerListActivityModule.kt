package com.memtrip.eosreach.app.blockproducerlist

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class BlockProducerListActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(BlockProducerListViewModel::class)
    internal abstract fun contributesBlockProducerListViewModel(viewModel: BlockProducerListViewModel): ViewModel
}