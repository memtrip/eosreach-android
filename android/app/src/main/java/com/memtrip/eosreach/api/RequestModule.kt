package com.memtrip.eosreach.api

import com.memtrip.eosreach.api.account.EosAccountRequest
import com.memtrip.eosreach.api.account.EosAccountRequestImpl
import com.memtrip.eosreach.api.accountforkey.AccountForPublicKeyRequest
import com.memtrip.eosreach.api.accountforkey.AccountsForPublicKeyRequestImpl
import com.memtrip.eosreach.api.actions.AccountActionsRequest
import com.memtrip.eosreach.api.actions.AccountActionsRequestImpl
import com.memtrip.eosreach.api.balance.AccountBalanceRequest
import com.memtrip.eosreach.api.balance.AccountBalanceRequestImp
import com.memtrip.eosreach.api.blockproducer.BlockProducerRequest
import com.memtrip.eosreach.api.blockproducer.BlockProducerRequestImpl
import com.memtrip.eosreach.api.eosprice.EosPriceRequest
import com.memtrip.eosreach.api.eosprice.EosPriceRequestImpl
import com.memtrip.eosreach.api.transfer.TransferRequest
import com.memtrip.eosreach.api.transfer.TransferRequestImpl
import com.memtrip.eosreach.api.vote.VoteRequest
import com.memtrip.eosreach.api.vote.VoteRequestImp
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

    @Binds
    internal abstract fun bindAccountBalancesRequest(
        accountBalanceRequest: AccountBalanceRequestImp
    ): AccountBalanceRequest

    @Binds
    internal abstract fun bindAccountActionsRequest(
        accountActionsRequest: AccountActionsRequestImpl
    ): AccountActionsRequest

    @Binds
    internal abstract fun bindTransferRequest(
        transferRequest: TransferRequestImpl
    ): TransferRequest

    @Binds
    internal abstract fun bindVoteRequest(
        transferRequest: VoteRequestImp
    ): VoteRequest

    @Binds
    internal abstract fun bindBlockProducerRequest(
        blockProducerRequest: BlockProducerRequestImpl
    ): BlockProducerRequest

    @Binds
    internal abstract fun bindEosPriceRequest(
        eosPriceRequest: EosPriceRequestImpl
    ): EosPriceRequest
}