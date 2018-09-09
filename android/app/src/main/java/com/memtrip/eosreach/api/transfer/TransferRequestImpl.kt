package com.memtrip.eosreach.api.transfer

import com.memtrip.eos.core.crypto.EosPrivateKey

import com.memtrip.eos.http.aggregation.transfer.TransferAggregate
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.app.price.BalanceParser
import com.memtrip.eosreach.utils.RxSchedulers
import com.memtrip.eosreach.utils.transactionDefaultExpiry

import io.reactivex.Single
import java.util.Calendar
import javax.inject.Inject

class TransferRequestImpl @Inject constructor(
    private val transferAggregate: TransferAggregate,
    private val rxSchedulers: RxSchedulers
) : TransferRequest {

    override fun transfer(
        fromAccount: String,
        toAccount: String,
        quantity: Balance,
        memo: String,
        authorizingPrivateKey: EosPrivateKey
    ): Single<Result<TransferReceipt, TransferError>> {
        return transferAggregate.transfer(
            TransferAggregate.Args(
                fromAccount,
                toAccount,
                BalanceParser.serializeForEosApiRequest(quantity),
                memo,
                fromAccount,
                authorizingPrivateKey,
                transactionDefaultExpiry()
            )
        ).map { response ->
            if (response.isSuccessful) {
                response.body
                Result(TransferReceipt(response.body!!.transaction_id))
            } else {
                Result<TransferReceipt, TransferError>(TransferError(response.errorBody!!))
            }
        }.observeOn(rxSchedulers.main()).subscribeOn(rxSchedulers.background())
    }
}