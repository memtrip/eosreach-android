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

import com.memtrip.eosreach.app.account.actions.ActionsActivity
import com.memtrip.eosreach.app.account.actions.ActionsActivityModule
import com.memtrip.eosreach.app.account.actions.ViewTransferActionActivity
import com.memtrip.eosreach.app.account.actions.ViewTransferActionActivityModule
import com.memtrip.eosreach.app.account.balance.BalanceFragment
import com.memtrip.eosreach.app.account.balance.BalanceFragmentModule
import com.memtrip.eosreach.app.account.resources.ResourcesFragment
import com.memtrip.eosreach.app.account.resources.ResourcesFragmentModule
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthConfirmActivity
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthConfirmActivityModule
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthManageActivity
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthManageActivityModule
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.DelegateBandwidthFormFragment
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.DelegateBandwidthFormFragmentModule
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.UnDelegateBandwidthFormFragment
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.UnDelegateBandwidthFormFragmentModule
import com.memtrip.eosreach.app.account.resources.manage.manageram.ManageRamActivity
import com.memtrip.eosreach.app.account.resources.manage.manageram.ManageRamActivityModule
import com.memtrip.eosreach.app.account.resources.manage.manageram.form.BuyRamFormFragment
import com.memtrip.eosreach.app.account.resources.manage.manageram.form.BuyRamFormFragmentModule
import com.memtrip.eosreach.app.account.resources.manage.manageram.form.SellRamFormFragment
import com.memtrip.eosreach.app.account.resources.manage.manageram.form.SellRamFormFragmentModule
import com.memtrip.eosreach.app.account.vote.VoteFragment
import com.memtrip.eosreach.app.account.vote.VoteFragmentModule
import com.memtrip.eosreach.app.account.vote.cast.CastVoteActivity
import com.memtrip.eosreach.app.account.vote.cast.CastVoteActivityModule
import com.memtrip.eosreach.app.account.vote.cast.producers.CastProducersVoteFragment
import com.memtrip.eosreach.app.account.vote.cast.producers.CastProducersVoteFragmentModule
import com.memtrip.eosreach.app.account.vote.cast.proxy.CastProxyVoteFragment
import com.memtrip.eosreach.app.account.vote.cast.proxy.CastProxyVoteFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AccountModule {

    @ContributesAndroidInjector(modules = [AccountActivityModule::class])
    internal abstract fun contributeAccountActivity(): AccountActivity

    @ContributesAndroidInjector(modules = [BalanceFragmentModule::class])
    internal abstract fun contributeBalanceFragments(): BalanceFragment

    @ContributesAndroidInjector(modules = [ResourcesFragmentModule::class])
    internal abstract fun contributeResourcesFragment(): ResourcesFragment

    @ContributesAndroidInjector(modules = [VoteFragmentModule::class])
    internal abstract fun contributeVoteFragment(): VoteFragment
}