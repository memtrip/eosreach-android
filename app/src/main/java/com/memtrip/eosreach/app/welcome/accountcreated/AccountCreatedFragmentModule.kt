package com.memtrip.eosreach.app.welcome.accountcreated

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AccountCreatedFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributesAccountCreatedFragment(): AccountCreatedFragment
}