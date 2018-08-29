package com.memtrip.eosreach.app.account

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class AccountActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    internal abstract fun contributesAccountViewModel(viewModel: AccountViewModel): ViewModel

    @ContributesAndroidInjector
    internal abstract fun contributeAccountActivity(): AccountActivity
}