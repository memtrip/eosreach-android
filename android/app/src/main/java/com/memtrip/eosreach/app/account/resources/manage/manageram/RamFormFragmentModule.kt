package com.memtrip.eosreach.app.account.resources.manage.manageram

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class RamFormFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(RamFormViewModel::class)
    internal abstract fun contributesManageRamFormViewModel(viewModel: RamFormViewModel): ViewModel
}