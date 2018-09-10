package com.memtrip.eosreach.api.vote

import com.memtrip.eos.http.aggregation.AggregateContext
import com.memtrip.eos.http.aggregation.vote.VoteAggregate

import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.db.account.GetAccountByName

import com.memtrip.eosreach.utils.RxSchedulers
import com.memtrip.eosreach.utils.transactionDefaultExpiry
import com.memtrip.eosreach.wallet.EosKeyManager
import io.reactivex.Single

import javax.inject.Inject

class VoteRequestImp @Inject constructor(
    private val getAccountByName: GetAccountByName,
    private val eosKeyManager: EosKeyManager,
    private val voteAggregate: VoteAggregate,
    private val rxSchedulers: RxSchedulers
) : VoteRequest {

    override fun vote(
        voter: String,
        producers: List<String>
    ): Single<Result<VoteReceipt, VoteError>> {
        return getAccountByName.select(voter).flatMap { accountEntity ->
            eosKeyManager.getPrivateKey(accountEntity.publicKey).flatMap { eosPrivateKey ->
                voteAggregate.vote(VoteAggregate.Args(
                    voter,
                    "",
                    producers
                ), AggregateContext(
                    voter,
                    eosPrivateKey,
                    transactionDefaultExpiry()
                )).map { response ->
                    if (response.isSuccessful) {
                        Result<VoteReceipt, VoteError>(VoteReceipt(response.body!!.transaction_id))
                    } else {
                        Result(VoteError(response.errorBody!!))
                    }
                }.onErrorReturn {
                    Result(VoteError(it.message!!))
                }
            }.onErrorReturn {
                Result(VoteError("Failed to decrypt private key"))
            }
        }.observeOn(rxSchedulers.main()).subscribeOn(rxSchedulers.background())
    }
}