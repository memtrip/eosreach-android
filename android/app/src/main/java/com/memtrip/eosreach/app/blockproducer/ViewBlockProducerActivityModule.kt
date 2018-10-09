package com.memtrip.eosreach.app.blockproducer

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class ViewBlockProducerActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(ViewBlockProducerViewModel::class)
    internal abstract fun contributesViewBlockProducerViewModel(viewModel: ViewBlockProducerViewModel): ViewModel
}