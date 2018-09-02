package com.memtrip.eosreach.app.welcome.createaccount

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WelcomeCreateAccountActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(WelcomeCreateAccountViewModel::class)
    internal abstract fun contributesWelcomeImportKeyViewModel(viewModel: WelcomeCreateAccountViewModel): ViewModel
}