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