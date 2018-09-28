package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.DelegateBandwidthFormFragment
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.DelegateBandwidthFormFragmentModule
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.UnDelegateBandwidthFormFragment
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.UnDelegateBandwidthFormFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BandwidthModule {

    @ContributesAndroidInjector(modules = [BandwidthManageActivityModule::class])
    internal abstract fun contributeManageBandwidthActivity(): BandwidthManageActivity

    @ContributesAndroidInjector(modules = [DelegateBandwidthFormFragmentModule::class])
    internal abstract fun contributeDelegateBandwidthFormFragment(): DelegateBandwidthFormFragment

    @ContributesAndroidInjector(modules = [UnDelegateBandwidthFormFragmentModule::class])
    internal abstract fun contributeUnDelegateBandwidthFormFragment(): UnDelegateBandwidthFormFragment

    @ContributesAndroidInjector(modules = [BandwidthConfirmActivityModule::class])
    internal abstract fun contributeBandwidthConfirmActivity(): BandwidthConfirmActivity
}