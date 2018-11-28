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
package com.memtrip.eosreach.api.transfer

import com.memtrip.eos.chain.actions.transaction.TransactionContext
import com.memtrip.eos.chain.actions.transaction.transfer.TransferChain
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eos.http.rpc.model.transaction.response.TransactionCommitted

import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.utils.RxSchedulers
import com.memtrip.eosreach.utils.transactionDefaultExpiry
import io.reactivex.Single
import javax.inject.Inject

class TransferRequestImpl @Inject constructor(
    private val transferChain: TransferChain,
    private val rxSchedulers: RxSchedulers
) : TransferRequest {

    override fun transfer(
        contract: String,
        fromAccount: String,
        toAccount: String,
        quantity: String,
        memo: String,
        authorizingPrivateKey: EosPrivateKey
    ): Single<Result<TransactionCommitted, TransferError>> {
        return transferChain.transfer(
            contract,
            TransferChain.Args(
                fromAccount,
                toAccount,
                quantity,
                memo
            ),
            TransactionContext(
                fromAccount,
                authorizingPrivateKey,
                transactionDefaultExpiry()
            )
        ).map { response ->
            if (response.isSuccessful) {
                response.body
                Result(response.body!!)
            } else {
                Result<TransactionCommitted, TransferError>(TransferError(response.errorBody!!))
            }
        }.observeOn(rxSchedulers.main()).subscribeOn(rxSchedulers.background())
    }
}