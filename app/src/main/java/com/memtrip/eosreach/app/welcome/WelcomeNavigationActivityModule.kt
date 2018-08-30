package com.memtrip.eosreach.app.welcome

import com.memtrip.eosreach.app.issue.createaccount.CreateAccountFragment
import com.memtrip.eosreach.app.issue.createaccount.CreateAccountFragmentModule
import com.memtrip.eosreach.app.welcome.entry.EntryFragment
import com.memtrip.eosreach.app.welcome.entry.EntryFragmentModule
import com.memtrip.eosreach.app.issue.importkey.ImportKeyFragment
import com.memtrip.eosreach.app.issue.importkey.ImportKeyFragmentModule
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

    @ContributesAndroidInjector(modules = [EntryFragmentModule::class])
    internal abstract fun contributeAccountListFragmentModule(): EntryFragment
}