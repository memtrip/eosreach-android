package com.memtrip.eosreach.app.welcome

import com.memtrip.eosreach.app.welcome.accountlist.AccountListFragment
import com.memtrip.eosreach.app.welcome.accountlist.AccountListFragmentModule
import com.memtrip.eosreach.app.welcome.createaccount.CreateAccountFragment
import com.memtrip.eosreach.app.welcome.createaccount.CreateAccountFragmentModule
import com.memtrip.eosreach.app.welcome.importkey.ImportKeyFragment
import com.memtrip.eosreach.app.welcome.importkey.ImportKeyFragmentModule
import com.memtrip.eosreach.app.welcome.keyimported.KeyImportedFragment
import com.memtrip.eosreach.app.welcome.keyimported.KeyImportedFragmentModule
import com.memtrip.eosreach.app.welcome.splash.SplashFragment
import com.memtrip.eosreach.app.welcome.splash.SplashFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class WelcomeNavigationActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributeWelcomeActivity(): WelcomeNavigationActivity

    @ContributesAndroidInjector(modules = [CreateAccountFragmentModule::class])
    internal abstract fun contributeCreateAccountFragmentModule(): CreateAccountFragment

    @ContributesAndroidInjector(modules = [ImportKeyFragmentModule::class])
    internal abstract fun contributeImportKeyFragmentModule(): ImportKeyFragment

    @ContributesAndroidInjector(modules = [KeyImportedFragmentModule::class])
    internal abstract fun contributeKeyImportedFragmentModule(): KeyImportedFragment

    @ContributesAndroidInjector(modules = [SplashFragmentModule::class])
    internal abstract fun contributeSplashFragmentModule(): SplashFragment

    @ContributesAndroidInjector(modules = [AccountListFragmentModule::class])
    internal abstract fun contributeAccountListFragmentModule(): AccountListFragment
}