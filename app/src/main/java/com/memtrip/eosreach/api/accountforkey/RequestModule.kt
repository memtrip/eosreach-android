package com.memtrip.eosreach.api.accountforkey

import dagger.Binds
import dagger.Module

@Module
internal abstract class RequestModule {

    @Binds
    internal abstract fun bindAccountForPublicKeyRequest(
        accountForPublicKeyRequest: AccountsForPublicKeyRequestImpl
    ): AccountForPublicKeyRequest
}