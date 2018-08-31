package com.memtrip.eosreach.app.welcome

import com.memtrip.eosreach.app.issue.createaccount.CreateAccountFragmentModule
import com.memtrip.eosreach.app.issue.importkey.ImportKeyFragmentModule
import com.memtrip.eosreach.app.welcome.createaccount.WelcomeCreateAccountFragment
import com.memtrip.eosreach.app.welcome.entry.EntryFragment
import com.memtrip.eosreach.app.welcome.entry.EntryFragmentModule
import com.memtrip.eosreach.app.welcome.importkey.WelcomeImportKeyFragment
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
    internal abstract fun contributeWelcomeCreateAccountFragmentModule(): WelcomeCreateAccountFragment

    @ContributesAndroidInjector(modules = [ImportKeyFragmentModule::class])
    internal abstract fun contributeWelcomeImportKeyFragmentModule(): WelcomeImportKeyFragment

    @ContributesAndroidInjector(modules = [KeyImportedFragmentModule::class])
    internal abstract fun contributeKeyImportedFragmentModule(): KeyImportedFragment

    @ContributesAndroidInjector(modules = [SplashFragmentModule::class])
    internal abstract fun contributeSplashFragmentModule(): SplashFragment

    @ContributesAndroidInjector(modules = [EntryFragmentModule::class])
    internal abstract fun contributeAccountListFragmentModule(): EntryFragment
}