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
package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.DelegateBandwidthFormFragment
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.DelegateBandwidthFormFragmentModule
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.UnDelegateBandwidthFormFragment
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.UnDelegateBandwidthFormFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BandwidthModule {

    @ContributesAndroidInjector(modules = [BandwidthManageActivityModule::class])
    internal abstract fun contributeManageBandwidthActivity(): BandwidthManageActivity

    @ContributesAndroidInjector(modules = [DelegateBandwidthFormFragmentModule::class])
    internal abstract fun contributeDelegateBandwidthFormFragment(): DelegateBandwidthFormFragment

    @ContributesAndroidInjector(modules = [UnDelegateBandwidthFormFragmentModule::class])
    internal abstract fun contributeUnDelegateBandwidthFormFragment(): UnDelegateBandwidthFormFragment

    @ContributesAndroidInjector(modules = [BandwidthConfirmActivityModule::class])
    internal abstract fun contributeBandwidthConfirmActivity(): BandwidthConfirmActivity

    @ContributesAndroidInjector(modules = [DelegateBandwidthListFragmentModule::class])
    internal abstract fun contributeDelegateBandwidthListFragment(): DelegateBandwidthListFragment
}