package com.memtrip.eosreach.app.blockproducerlist

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BlockProducerListModule {

    @ContributesAndroidInjector(modules = [BlockProducerListActivityModule::class])
    internal abstract fun contributeBlockProducerListActivity(): BlockProducerListActivity
}