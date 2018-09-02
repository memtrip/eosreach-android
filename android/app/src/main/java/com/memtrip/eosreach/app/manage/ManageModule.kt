package com.memtrip.eosreach.app.manage

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ManageModule {

    @ContributesAndroidInjector(modules = [ManageCreateAccountActivityModule::class])
    internal abstract fun contributeManageCreateAccountActivityModule(): ManageCreateAccountActivity

    @ContributesAndroidInjector(modules = [ManageImportKeyActivityModule::class])
    internal abstract fun contributeManageImportKeyActivityModule(): ManageImportKeyActivity
}