package com.memtrip.eosreach.app.welcome.splash

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SplashFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributesSplashFragment(): SplashFragment
}