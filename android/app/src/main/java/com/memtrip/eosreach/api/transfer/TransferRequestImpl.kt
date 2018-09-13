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
        fromAccount: String,
        toAccount: String,
        quantity: String,
        memo: String,
        authorizingPrivateKey: EosPrivateKey
    ): Single<Result<TransactionCommitted, TransferError>> {
        return transferChain.transfer(
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