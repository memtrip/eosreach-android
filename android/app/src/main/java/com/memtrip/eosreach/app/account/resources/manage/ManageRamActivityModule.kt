package com.memtrip.eosreach.app.account.resources.manage

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class ManageRamActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(ManageRamViewModel::class)
    internal abstract fun contributesManageRamViewModel(viewModel: ManageRamViewModel): ViewModel
}