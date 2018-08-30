package com.memtrip.eosreach.app.account

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey
import com.memtrip.eosreach.app.account.balance.BalanceFragment
import com.memtrip.eosreach.app.account.balance.BalanceFragmentModule
import com.memtrip.eosreach.app.account.resources.ResourcesFragment
import com.memtrip.eosreach.app.account.resources.ResourcesFragmentModule
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

    @ContributesAndroidInjector(modules = [BalanceFragmentModule::class])
    internal abstract fun contributeBalanceFragmentModule(): BalanceFragment

    @ContributesAndroidInjector(modules = [ResourcesFragmentModule::class])
    internal abstract fun contributeResourcesFragmentModule(): ResourcesFragment
}