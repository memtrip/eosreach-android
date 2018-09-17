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