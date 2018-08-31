package com.memtrip.eosreach.app.account.resources

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class ResourcesFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(ResourcesViewModel::class)
    internal abstract fun contributesResourcesViewModel(viewModel: ResourcesViewModel): ViewModel
}