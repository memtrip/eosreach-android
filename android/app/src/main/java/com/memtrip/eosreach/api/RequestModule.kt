/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.memtrip.eosreach.api

import com.memtrip.eosreach.api.account.EosAccountRequest
import com.memtrip.eosreach.api.account.EosAccountRequestImpl
import com.memtrip.eosreach.api.accountforkey.AccountForPublicKeyRequest
import com.memtrip.eosreach.api.accountforkey.AccountsForPublicKeyRequestImpl
import com.memtrip.eosreach.api.actions.AccountActionsRequest
import com.memtrip.eosreach.api.actions.AccountActionsRequestImpl
import com.memtrip.eosreach.api.balance.AccountBalanceRequest
import com.memtrip.eosreach.api.balance.AccountBalanceRequestImp
import com.memtrip.eosreach.api.bandwidth.BandwidthRequest
import com.memtrip.eosreach.api.bandwidth.BandwidthRequestImpl
import com.memtrip.eosreach.api.blockproducer.BlockProducerRequest
import com.memtrip.eosreach.api.blockproducer.BlockProducerRequestImpl
import com.memtrip.eosreach.api.customtokens.CustomTokensRequest
import com.memtrip.eosreach.api.customtokens.CustomTokensRequestImpl
import com.memtrip.eosreach.api.eoscreateaccount.EosCreateAccountRequest
import com.memtrip.eosreach.api.eoscreateaccount.EosCreateAccountRequestImpl
import com.memtrip.eosreach.api.eosprice.EosPriceRequest
import com.memtrip.eosreach.api.eosprice.EosPriceRequestImpl
import com.memtrip.eosreach.api.proxyvoter.ProxyVoterRequest
import com.memtrip.eosreach.api.proxyvoter.ProxyVoterRequestImpl
import com.memtrip.eosreach.api.ram.RamRequest
import com.memtrip.eosreach.api.ram.RamRequestImpl
import com.memtrip.eosreach.api.ramprice.RamPriceRequest
import com.memtrip.eosreach.api.ramprice.RamPriceRequestImpl
import com.memtrip.eosreach.api.transfer.TransferRequest
import com.memtrip.eosreach.api.transfer.TransferRequestImpl
import com.memtrip.eosreach.api.vote.VoteRequest
import com.memtrip.eosreach.api.vote.VoteRequestImpl
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
    internal abstract fun bindCustomTokensRequest(
        customTokensRequest: CustomTokensRequestImpl
    ): CustomTokensRequest

    @Binds
    internal abstract fun bindGetRamPriceRequest(
        ramPriceRequest: RamPriceRequestImpl
    ): RamPriceRequest

    @Binds
    internal abstract fun bindTransferRequest(
        transferRequest: TransferRequestImpl
    ): TransferRequest

    @Binds
    internal abstract fun bindBandwidthRequest(
        bandwidthRequest: BandwidthRequestImpl
    ): BandwidthRequest

    @Binds
    internal abstract fun bindRamRequest(
        ramRequest: RamRequestImpl
    ): RamRequest

    @Binds
    internal abstract fun bindVoteRequest(
        voteRequest: VoteRequestImpl
    ): VoteRequest

    @Binds
    internal abstract fun bindBlockProducerRequest(
        blockProducerRequest: BlockProducerRequestImpl
    ): BlockProducerRequest

    @Binds
    internal abstract fun bindProxyVoterRequest(
        bindProxyVoterRequest: ProxyVoterRequestImpl
    ): ProxyVoterRequest

    @Binds
    internal abstract fun bindEosPriceRequest(
        eosPriceRequest: EosPriceRequestImpl
    ): EosPriceRequest

    @Binds
    internal abstract fun bindEosCreateAccountRequest(
        eosCreateAccountRequest: EosCreateAccountRequestImpl
    ): EosCreateAccountRequest
}