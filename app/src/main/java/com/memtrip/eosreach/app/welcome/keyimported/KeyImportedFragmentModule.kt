package com.memtrip.eosreach.app.welcome.keyimported

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class KeyImportedFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(KeyImportedViewModel::class)
    internal abstract fun contributesKeyImportedViewModel(viewModel: KeyImportedViewModel): ViewModel
}