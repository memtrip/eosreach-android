package com.memtrip.eosreach.app.manage

import com.memtrip.eosreach.app.issue.createaccount.CreateAccountFragmentModule


import com.memtrip.eosreach.app.issue.importkey.ImportKeyFragmentModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ManageNavigationActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributeManageNavigationActivity(): ManageNavigationActivity

    @ContributesAndroidInjector(modules = [CreateAccountFragmentModule::class])
    internal abstract fun contributeManageCreateAccountFragmentModule(): ManageCreateAccountFragment

    @ContributesAndroidInjector(modules = [ImportKeyFragmentModule::class])
    internal abstract fun contributeManageImportKeyFragmentModule(): ManageImportKeyFragment
}