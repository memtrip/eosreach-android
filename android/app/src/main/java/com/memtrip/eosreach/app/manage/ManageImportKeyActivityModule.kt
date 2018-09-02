package com.memtrip.eosreach.app.manage

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ManageImportKeyActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(ManageImportKeyViewModel::class)
    internal abstract fun contributesManageImportKeyViewModel(viewModel: ManageImportKeyViewModel): ViewModel
}