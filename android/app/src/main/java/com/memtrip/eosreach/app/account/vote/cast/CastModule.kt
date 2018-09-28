package com.memtrip.eosreach.app.account.vote.cast

import com.memtrip.eosreach.app.account.vote.cast.producers.CastProducersVoteFragment
import com.memtrip.eosreach.app.account.vote.cast.producers.CastProducersVoteFragmentModule
import com.memtrip.eosreach.app.account.vote.cast.proxy.CastProxyVoteFragment
import com.memtrip.eosreach.app.account.vote.cast.proxy.CastProxyVoteFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CastModule {

    @ContributesAndroidInjector(modules = [CastVoteActivityModule::class])
    internal abstract fun contributeCastVoteActivity(): CastVoteActivity

    @ContributesAndroidInjector(modules = [CastProxyVoteFragmentModule::class])
    internal abstract fun contributeCastProxyVoteFragment(): CastProxyVoteFragment

    @ContributesAndroidInjector(modules = [CastProducersVoteFragmentModule::class])
    internal abstract fun contributeCastProducersVoteFragment(): CastProducersVoteFragment
}