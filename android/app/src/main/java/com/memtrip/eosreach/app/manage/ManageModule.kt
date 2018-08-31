package com.memtrip.eosreach.app.manage

import com.memtrip.eosreach.app.issue.createaccount.CreateAccountFragmentModule

import com.memtrip.eosreach.app.issue.importkey.ImportKeyActivityModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ManageModule {

    @ContributesAndroidInjector(modules = [CreateAccountFragmentModule::class])
    internal abstract fun contributeManageCreateAccountFragmentModule(): ManageCreateAccountActivity

    @ContributesAndroidInjector(modules = [ImportKeyActivityModule::class])
    internal abstract fun contributeManageImportKeyFragmentModule(): ManageImportKeyActivity
}