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
package com.memtrip.eosreach.app.account.resources.manage.manageram

import com.memtrip.eosreach.app.account.resources.manage.manageram.form.BuyRamFormFragment
import com.memtrip.eosreach.app.account.resources.manage.manageram.form.BuyRamFormFragmentModule
import com.memtrip.eosreach.app.account.resources.manage.manageram.form.SellRamFormFragment
import com.memtrip.eosreach.app.account.resources.manage.manageram.form.SellRamFormFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ManageRamModule {

    @ContributesAndroidInjector(modules = [ManageRamActivityModule::class])
    internal abstract fun contributeManageRamActivity(): ManageRamActivity

    @ContributesAndroidInjector(modules = [BuyRamFormFragmentModule::class])
    internal abstract fun contributeBuyRamFormFragment(): BuyRamFormFragment

    @ContributesAndroidInjector(modules = [SellRamFormFragmentModule::class])
    internal abstract fun contributeSellRamFormFragmentModule(): SellRamFormFragment

    @ContributesAndroidInjector(modules = [RamConfirmActivityModule::class])
    internal abstract fun contributeRamConfirmActivity(): RamConfirmActivity
}