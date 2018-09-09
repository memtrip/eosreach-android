package com.memtrip.eosreach.app.account

import com.memtrip.eosreach.app.account.actions.ActionsActivity
import com.memtrip.eosreach.app.account.actions.ActionsActivityModule
import com.memtrip.eosreach.app.account.balance.BalanceFragment
import com.memtrip.eosreach.app.account.balance.BalanceFragmentModule
import com.memtrip.eosreach.app.account.resources.ResourcesFragment
import com.memtrip.eosreach.app.account.resources.ResourcesFragmentModule
import com.memtrip.eosreach.app.account.vote.cast.CastVoteActivity
import com.memtrip.eosreach.app.account.vote.cast.CastVoteActivityModule
import com.memtrip.eosreach.app.account.vote.VoteFragment
import com.memtrip.eosreach.app.account.vote.VoteFragmentModule
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
}