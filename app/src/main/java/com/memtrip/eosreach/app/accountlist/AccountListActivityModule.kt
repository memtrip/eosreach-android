package com.memtrip.eosreach.app.accountlist

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class AccountListActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(AccountListViewModel::class)
    internal abstract fun contributesAccountListViewModel(viewModel: AccountListViewModel): ViewModel

    @ContributesAndroidInjector
    internal abstract fun contributeAccountListActivity(): AccountListActivity
}