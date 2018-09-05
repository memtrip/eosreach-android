package com.memtrip.eosreach.app.settings.viewprivatekeys

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class ViewPrivateKeysActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(ViewPrivateKeysViewModel::class)
    internal abstract fun contributesViewPrivateKeysViewModel(viewModel: ViewPrivateKeysViewModel): ViewModel
}