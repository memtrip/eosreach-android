package com.memtrip.eosreach.app.account.resources.manage.manageram.form

import androidx.lifecycle.ViewModel

import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SellRamFormFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(SellRamFormViewModel::class)
    internal abstract fun contributesSellRamFormViewModel(viewModel: SellRamFormViewModel): ViewModel
}