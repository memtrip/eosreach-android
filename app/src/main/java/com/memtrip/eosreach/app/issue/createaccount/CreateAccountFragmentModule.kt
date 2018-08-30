package com.memtrip.eosreach.app.issue.createaccount

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CreateAccountFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(CreateAccountViewModel::class)
    internal abstract fun contributesCreateAccountViewModel(viewModel: CreateAccountViewModel): ViewModel
}