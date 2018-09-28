package com.memtrip.eosreach.api.ram

import com.memtrip.eos.chain.actions.transaction.TransactionContext
import com.memtrip.eos.chain.actions.transaction.account.BuyRamBytesChain
import com.memtrip.eos.chain.actions.transaction.account.SellRamChain
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.transfer.ActionReceipt
import com.memtrip.eosreach.utils.RxSchedulers
import com.memtrip.eosreach.utils.transactionDefaultExpiry
import io.reactivex.Single
import javax.inject.Inject

class RamRequestImpl @Inject internal constructor(
    private val buyRamBytesChain: BuyRamBytesChain,
    private val sellRamChain: SellRamChain,
    private val rxSchedulers: RxSchedulers
) : RamRequest {

    override fun buy(
        receiver: String,
        kb: Double,
        authorizingPrivateKey: EosPrivateKey
    ): Single<Result<ActionReceipt, RamError>> {
        return buyRamBytesChain.buyRamBytes(BuyRamBytesChain.Args(
            receiver,
            (kb*1000).toLong()),
            TransactionContext(
                receiver,
                authorizingPrivateKey,
                transactionDefaultExpiry()
            )
        ).observeOn(rxSchedulers.main()).subscribeOn(rxSchedulers.background()).map { response ->
            if (response.isSuccessful) {
                Result<ActionReceipt, RamError>(ActionReceipt(
                    response.body!!.transaction_id,
                    receiver))
            } else {
                Result(RamError(response.errorBody!!))
            }
        }.onErrorReturn {
            Result(RamError(it.message!!))
        }
    }

    override fun sell(
        account: String,
        kb: Double,
        authorizingPrivateKey: EosPrivateKey
    ): Single<Result<ActionReceipt, RamError>> {
        return sellRamChain.sellRam(SellRamChain.Args((kb*1000).toLong()),
            TransactionContext(
                account,
                authorizingPrivateKey,
                transactionDefaultExpiry()
            )
        ).observeOn(rxSchedulers.main()).subscribeOn(rxSchedulers.background()).map { response ->
            if (response.isSuccessful) {
                Result<ActionReceipt, RamError>(ActionReceipt(
                    response.body!!.transaction_id,
                    account))
            } else {
                Result(RamError(response.errorBody!!))
            }
        }.onErrorReturn {
            Result(RamError(it.message!!))
        }
    }
}