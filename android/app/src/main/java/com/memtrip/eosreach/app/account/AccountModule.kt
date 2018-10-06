/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.memtrip.eosreach.app.account

import com.memtrip.eosreach.app.account.balance.BalanceFragment
import com.memtrip.eosreach.app.account.balance.BalanceFragmentModule
import com.memtrip.eosreach.app.account.balance.DefaultBalanceFragment
import com.memtrip.eosreach.app.account.balance.ReadOnlyBalanceFragment
import com.memtrip.eosreach.app.account.navigation.AccountNavigationFragment
import com.memtrip.eosreach.app.account.navigation.AccountNavigationModule
import com.memtrip.eosreach.app.account.resources.DefaultResourcesFragment
import com.memtrip.eosreach.app.account.resources.ReadOnlyResourcesFragment
import com.memtrip.eosreach.app.account.resources.ResourcesFragment
import com.memtrip.eosreach.app.account.resources.ResourcesFragmentModule
import com.memtrip.eosreach.app.account.vote.DefaultVoteFragment
import com.memtrip.eosreach.app.account.vote.ReadOnlyVoteFragment
import com.memtrip.eosreach.app.account.vote.VoteFragment
import com.memtrip.eosreach.app.account.vote.VoteFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AccountModule {

    @ContributesAndroidInjector(modules = [AccountActivityModule::class])
    internal abstract fun contributeDefaultAccountActivity(): DefaultAccountActivity

    @ContributesAndroidInjector(modules = [AccountActivityModule::class])
    internal abstract fun contributeReadOnlyAccountActivity(): ReadonlyAccountActivity

    @ContributesAndroidInjector(modules = [BalanceFragmentModule::class])
    internal abstract fun contributeDefaultBalanceFragment(): DefaultBalanceFragment

    @ContributesAndroidInjector(modules = [BalanceFragmentModule::class])
    internal abstract fun contributeReadOnlyBalanceFragment(): ReadOnlyBalanceFragment

    @ContributesAndroidInjector(modules = [ResourcesFragmentModule::class])
    internal abstract fun contributeDefaultResourcesFragment(): DefaultResourcesFragment

    @ContributesAndroidInjector(modules = [ResourcesFragmentModule::class])
    internal abstract fun contributeReadOnlyResourcesFragment(): ReadOnlyResourcesFragment

    @ContributesAndroidInjector(modules = [VoteFragmentModule::class])
    internal abstract fun contributeDefaultVoteFragment(): DefaultVoteFragment

    @ContributesAndroidInjector(modules = [VoteFragmentModule::class])
    internal abstract fun contributeReadOnlyVoteFragment(): ReadOnlyVoteFragment

    @ContributesAndroidInjector(modules = [AccountNavigationModule::class])
    internal abstract fun contributeAccountNavigationFragment(): AccountNavigationFragment
}