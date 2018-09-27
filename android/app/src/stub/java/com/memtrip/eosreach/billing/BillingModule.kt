package com.memtrip.eosreach.billing

import dagger.Binds
import dagger.Module

@Module
abstract class BillingModule {

    @Binds
    internal abstract fun contributesBilling(billing: BillingStub): Billing
}