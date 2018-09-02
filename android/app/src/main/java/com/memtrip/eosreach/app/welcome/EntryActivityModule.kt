package com.memtrip.eosreach.app.welcome

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class EntryActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(EntryViewModel::class)
    internal abstract fun contributesAccountListViewModel(viewModel: EntryViewModel): ViewModel
}