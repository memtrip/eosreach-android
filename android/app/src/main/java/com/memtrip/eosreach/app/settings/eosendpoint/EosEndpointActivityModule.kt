package com.memtrip.eosreach.app.settings.eosendpoint

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class EosEndpointActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(EosEndpointViewModel::class)
    internal abstract fun contributesEosEndpointViewModel(viewModel: EosEndpointViewModel): ViewModel
}