package com.memtrip.eosreach.app.welcome.newaccount

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class NewAccountFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributesNewAccountFragment(): NewAccountFragment
}