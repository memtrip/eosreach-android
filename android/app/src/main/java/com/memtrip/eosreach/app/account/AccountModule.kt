package com.memtrip.eosreach.app.account

import com.memtrip.eosreach.app.account.actions.ActionsActivity
import com.memtrip.eosreach.app.account.actions.ActionsActivityModule
import com.memtrip.eosreach.app.account.actions.ViewTransferActionActivity
import com.memtrip.eosreach.app.account.actions.ViewTransferActionActivityModule
import com.memtrip.eosreach.app.account.balance.BalanceFragment
import com.memtrip.eosreach.app.account.balance.BalanceFragmentModule
import com.memtrip.eosreach.app.account.resources.ResourcesFragment
import com.memtrip.eosreach.app.account.resources.ResourcesFragmentModule
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthConfirmActivity
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthConfirmActivityModule
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthManageActivity
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.BandwidthManageActivityModule
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.DelegateBandwidthFormFragment
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.DelegateBandwidthFormFragmentModule
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.UnDelegateBandwidthFormFragment
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.UnDelegateBandwidthFormFragmentModule
import com.memtrip.eosreach.app.account.resources.manage.manageram.ManageRamActivity
import com.memtrip.eosreach.app.account.resources.manage.manageram.ManageRamActivityModule
import com.memtrip.eosreach.app.account.resources.manage.manageram.form.BuyRamFormFragment
import com.memtrip.eosreach.app.account.resources.manage.manageram.form.BuyRamFormFragmentModule
import com.memtrip.eosreach.app.account.resources.manage.manageram.form.SellRamFormFragment
import com.memtrip.eosreach.app.account.resources.manage.manageram.form.SellRamFormFragmentModule
import com.memtrip.eosreach.app.account.vote.VoteFragment
import com.memtrip.eosreach.app.account.vote.VoteFragmentModule
import com.memtrip.eosreach.app.account.vote.cast.CastVoteActivity
import com.memtrip.eosreach.app.account.vote.cast.CastVoteActivityModule
import com.memtrip.eosreach.app.account.vote.cast.producers.CastProducersVoteFragment
import com.memtrip.eosreach.app.account.vote.cast.producers.CastProducersVoteFragmentModule
import com.memtrip.eosreach.app.account.vote.cast.proxy.CastProxyVoteFragment
import com.memtrip.eosreach.app.account.vote.cast.proxy.CastProxyVoteFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AccountModule {

    @ContributesAndroidInjector(modules = [AccountActivityModule::class])
    internal abstract fun contributeAccountActivity(): AccountActivity

    @ContributesAndroidInjector(modules = [BalanceFragmentModule::class])
    internal abstract fun contributeBalanceFragments(): BalanceFragment

    @ContributesAndroidInjector(modules = [ResourcesFragmentModule::class])
    internal abstract fun contributeResourcesFragment(): ResourcesFragment

    @ContributesAndroidInjector(modules = [BandwidthManageActivityModule::class])
    internal abstract fun contributeManageBandwidthActivity(): BandwidthManageActivity

    @ContributesAndroidInjector(modules = [DelegateBandwidthFormFragmentModule::class])
    internal abstract fun contributeDelegateBandwidthFormFragment(): DelegateBandwidthFormFragment

    @ContributesAndroidInjector(modules = [UnDelegateBandwidthFormFragmentModule::class])
    internal abstract fun contributeUnDelegateBandwidthFormFragment(): UnDelegateBandwidthFormFragment

    @ContributesAndroidInjector(modules = [BandwidthConfirmActivityModule::class])
    internal abstract fun contributeBandwidthConfirmActivity(): BandwidthConfirmActivity

    @ContributesAndroidInjector(modules = [ManageRamActivityModule::class])
    internal abstract fun contributeManageRamActivity(): ManageRamActivity

    @ContributesAndroidInjector(modules = [BuyRamFormFragmentModule::class])
    internal abstract fun contributeBuyRamFormFragment(): BuyRamFormFragment

    @ContributesAndroidInjector(modules = [SellRamFormFragmentModule::class])
    internal abstract fun contributeSellRamFormFragmentModule(): SellRamFormFragment

    @ContributesAndroidInjector(modules = [VoteFragmentModule::class])
    internal abstract fun contributeVoteFragment(): VoteFragment

    @ContributesAndroidInjector(modules = [CastVoteActivityModule::class])
    internal abstract fun contributeCastVoteActivity(): CastVoteActivity

    @ContributesAndroidInjector(modules = [CastProxyVoteFragmentModule::class])
    internal abstract fun contributeCastProxyVoteFragment(): CastProxyVoteFragment

    @ContributesAndroidInjector(modules = [CastProducersVoteFragmentModule::class])
    internal abstract fun contributeCastProducersVoteFragment(): CastProducersVoteFragment

    @ContributesAndroidInjector(modules = [ActionsActivityModule::class])
    internal abstract fun contributeActionsActivity(): ActionsActivity

    @ContributesAndroidInjector(modules = [ViewTransferActionActivityModule::class])
    internal abstract fun contributeViewTransferActionActivity(): ViewTransferActionActivity
}