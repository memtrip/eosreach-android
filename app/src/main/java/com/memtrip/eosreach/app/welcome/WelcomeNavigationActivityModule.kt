package com.memtrip.eosreach.app.welcome

import com.memtrip.eosreach.app.welcome.accountcreated.AccountCreatedFragment
import com.memtrip.eosreach.app.welcome.accountcreated.AccountCreatedFragmentModule
import com.memtrip.eosreach.app.welcome.importkey.ImportKeyFragment
import com.memtrip.eosreach.app.welcome.importkey.ImportKeyFragmentModule
import com.memtrip.eosreach.app.welcome.newaccount.NewAccountFragment
import com.memtrip.eosreach.app.welcome.newaccount.NewAccountFragmentModule
import com.memtrip.eosreach.app.welcome.splash.SplashFragment
import com.memtrip.eosreach.app.welcome.splash.SplashFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class WelcomeNavigationActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributeWelcomeActivity(): WelcomeNavigationActivity

    @ContributesAndroidInjector(modules = [AccountCreatedFragmentModule::class])
    internal abstract fun contributeAccountCreatedFragmentModule(): AccountCreatedFragment

    @ContributesAndroidInjector(modules = [ImportKeyFragmentModule::class])
    internal abstract fun contributeImportKeyFragmentModule(): ImportKeyFragment

    @ContributesAndroidInjector(modules = [NewAccountFragmentModule::class])
    internal abstract fun contributeNewAccountFragmentModule(): NewAccountFragment

    @ContributesAndroidInjector(modules = [SplashFragmentModule::class])
    internal abstract fun contributeSplashFragmentModule(): SplashFragment
}