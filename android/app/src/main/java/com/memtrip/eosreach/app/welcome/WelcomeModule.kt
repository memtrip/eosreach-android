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
package com.memtrip.eosreach.app.welcome

import com.memtrip.eosreach.app.welcome.createaccount.WelcomeCreateAccountActivity
import com.memtrip.eosreach.app.welcome.createaccount.WelcomeCreateAccountActivityModule
import com.memtrip.eosreach.app.welcome.importkey.WelcomeImportKeyActivity
import com.memtrip.eosreach.app.welcome.importkey.WelcomeImportKeyActivityModule
import com.memtrip.eosreach.app.welcome.splash.SplashActivity
import com.memtrip.eosreach.app.welcome.splash.SplashActivityModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class WelcomeModule {

    @ContributesAndroidInjector(modules = [EntryActivityModule::class])
    internal abstract fun contributeEntryActivity(): EntryActivity

    @ContributesAndroidInjector(modules = [SplashActivityModule::class])
    internal abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [WelcomeCreateAccountActivityModule::class])
    internal abstract fun contributeWelcomeCreateAccountActivityModule(): WelcomeCreateAccountActivity

    @ContributesAndroidInjector(modules = [WelcomeImportKeyActivityModule::class])
    internal abstract fun contributeWelcomeImportKeyActivityModule(): WelcomeImportKeyActivity
}