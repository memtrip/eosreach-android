package com.memtrip.eosreach.app.account.actions

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActionsModule {

    @ContributesAndroidInjector(modules = [ActionsActivityModule::class])
    internal abstract fun contributeActionsActivity(): ActionsActivity

    @ContributesAndroidInjector(modules = [ViewTransferActionActivityModule::class])
    internal abstract fun contributeViewTransferActionActivity(): ViewTransferActionActivity
}