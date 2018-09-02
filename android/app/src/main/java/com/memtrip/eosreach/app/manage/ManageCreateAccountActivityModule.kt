package com.memtrip.eosreach.app.manage

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ManageCreateAccountActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(ManageCreateAccountViewModel::class)
    internal abstract fun contributesManageCreateAccountViewModel(viewModel: ManageCreateAccountViewModel): ViewModel
}