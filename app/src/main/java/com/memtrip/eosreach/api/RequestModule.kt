package com.memtrip.eosreach.api

import com.memtrip.eosreach.api.account.EosAccountRequest
import com.memtrip.eosreach.api.account.EosAccountRequestImpl
import com.memtrip.eosreach.api.accountforkey.AccountForPublicKeyRequest
import com.memtrip.eosreach.api.accountforkey.AccountsForPublicKeyRequestImpl
import dagger.Binds
import dagger.Module

@Module
internal abstract class RequestModule {

    @Binds
    internal abstract fun bindAccountForPublicKeyRequest(
        accountForPublicKeyRequest: AccountsForPublicKeyRequestImpl
    ): AccountForPublicKeyRequest

    @Binds
    internal abstract fun bindAccountRequest(
        accountRequest: EosAccountRequestImpl
    ): EosAccountRequest
}