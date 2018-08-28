package com.memtrip.eosreach.app.welcome.accountlist

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module

import dagger.multibindings.IntoMap

@Module
abstract class AccountListFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(AccountListViewModel::class)
    internal abstract fun contributesAccountListViewModel(viewModel: AccountListViewModel): ViewModel
}