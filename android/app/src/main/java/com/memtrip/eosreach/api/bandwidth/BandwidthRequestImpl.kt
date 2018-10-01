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
package com.memtrip.eosreach.api.bandwidth

import com.memtrip.eos.chain.actions.transaction.TransactionContext
import com.memtrip.eos.chain.actions.transaction.account.DelegateBandwidthChain
import com.memtrip.eos.chain.actions.transaction.account.UnDelegateBandwidthChain
import com.memtrip.eos.core.crypto.EosPrivateKey

import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.transfer.ActionReceipt
import com.memtrip.eosreach.utils.RxSchedulers
import com.memtrip.eosreach.utils.transactionDefaultExpiry
import io.reactivex.Single
import javax.inject.Inject

class BandwidthRequestImpl @Inject internal constructor(
    private val delegateBandwidthChain: DelegateBandwidthChain,
    private val unDelegateBandwidthChain: UnDelegateBandwidthChain,
    private val rxSchedulers: RxSchedulers
) : BandwidthRequest {

    override fun delegate(
        fromAccount: String,
        netAmount: String,
        cpuAmount: String,
        authorizingPrivateKey: EosPrivateKey
    ): Single<Result<ActionReceipt, BandwidthError>> {
        return delegateBandwidthChain.delegateBandwidth(
            DelegateBandwidthChain.Args(
                fromAccount,
                fromAccount,
                netAmount,
                cpuAmount,
                false
            ),
            TransactionContext(
                fromAccount,
                authorizingPrivateKey,
                transactionDefaultExpiry()
            )
        ).map { response ->
            if (response.isSuccessful) {
                Result<ActionReceipt, BandwidthError>(ActionReceipt(
                    response.body!!.transaction_id,
                    fromAccount
                ))
            } else {
                Result(BandwidthError(response.errorBody!!))
            }
        }.observeOn(rxSchedulers.main()).subscribeOn(rxSchedulers.background())
    }

    override fun unDelegate(
        fromAccount: String,
        netAmount: String,
        cpuAmount: String,
        authorizingPrivateKey: EosPrivateKey
    ): Single<Result<ActionReceipt, BandwidthError>> {
        return unDelegateBandwidthChain.unDelegateBandwidth(
            UnDelegateBandwidthChain.Args(
                fromAccount,
                fromAccount,
                netAmount,
                cpuAmount
            ),
            TransactionContext(
                fromAccount,
                authorizingPrivateKey,
                transactionDefaultExpiry()
            )
        ).map { response ->
            if (response.isSuccessful) {
                Result<ActionReceipt, BandwidthError>(ActionReceipt(
                    response.body!!.transaction_id,
                    fromAccount
                ))
            } else {
                Result(BandwidthError(response.errorBody!!))
            }
        }.observeOn(rxSchedulers.main()).subscribeOn(rxSchedulers.background())
    }
}