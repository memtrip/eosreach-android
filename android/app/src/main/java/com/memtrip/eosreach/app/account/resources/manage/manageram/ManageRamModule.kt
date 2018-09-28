package com.memtrip.eosreach.app.account.resources.manage.manageram

import com.memtrip.eosreach.app.account.resources.manage.manageram.form.BuyRamFormFragment
import com.memtrip.eosreach.app.account.resources.manage.manageram.form.BuyRamFormFragmentModule
import com.memtrip.eosreach.app.account.resources.manage.manageram.form.SellRamFormFragment
import com.memtrip.eosreach.app.account.resources.manage.manageram.form.SellRamFormFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ManageRamModule {

    @ContributesAndroidInjector(modules = [ManageRamActivityModule::class])
    internal abstract fun contributeManageRamActivity(): ManageRamActivity

    @ContributesAndroidInjector(modules = [BuyRamFormFragmentModule::class])
    internal abstract fun contributeBuyRamFormFragment(): BuyRamFormFragment

    @ContributesAndroidInjector(modules = [SellRamFormFragmentModule::class])
    internal abstract fun contributeSellRamFormFragmentModule(): SellRamFormFragment

    @ContributesAndroidInjector(modules = [RamConfirmActivityModule::class])
    internal abstract fun contributeRamConfirmActivity(): RamConfirmActivity
}