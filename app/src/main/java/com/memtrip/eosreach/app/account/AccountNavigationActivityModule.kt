package com.memtrip.eosreach.app.account

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AccountNavigationActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributeAccountListNavigationActivity(): AccountNavigationActivity
}
