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

class VoteRequestImpl @Inject constructor(
    private val getAccountByName: GetAccountByName,
    private val eosKeyManager: EosKeyManager,
    private val voteAggregate: VoteAggregate,
    private val rxSchedulers: RxSchedulers
) : VoteRequest {

    override fun voteForProducer(
        voterAccountName: String,
        producers: List<String>
    ): Single<Result<VoteReceipt, VoteError>> {
        return getAccountByName.select(voterAccountName).flatMap { accountEntity ->
            eosKeyManager.getPrivateKey(accountEntity.publicKey).flatMap { eosPrivateKey ->
                voteAggregate.vote(VoteAggregate.Args(
                    voterAccountName,
                    "",
                    producers
                ), AggregateContext(
                    voterAccountName,
                    eosPrivateKey,
                    transactionDefaultExpiry()
                )).observeOn(rxSchedulers.main())
                    .subscribeOn(rxSchedulers.background())
                    .map { response ->
                        if (response.isSuccessful) {
                            Result<VoteReceipt, VoteError>(VoteReceipt(response.body!!.transaction_id))
                        } else {
                            Result(VoteError(response.errorBody!!))
                        }
                    }.onErrorReturn {
                        Result(VoteError(it.message!!))
                    }
            }.onErrorReturn {
                Result(VoteError(it.message!!))
            }
        }
    }

    override fun voteForProxy(
        voterAccountName: String,
        proxyVoteAccountName: String
    ): Single<Result<VoteReceipt, VoteError>> {
        return getAccountByName.select(voterAccountName)
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
            .flatMap { accountEntity ->
                eosKeyManager.getPrivateKey(accountEntity.publicKey).flatMap { eosPrivateKey ->
                    voteAggregate.vote(VoteAggregate.Args(
                        voterAccountName,
                        proxyVoteAccountName,
                        emptyList()
                    ), AggregateContext(
                        voterAccountName,
                        eosPrivateKey,
                        transactionDefaultExpiry()
                    )).observeOn(rxSchedulers.main())
                        .subscribeOn(rxSchedulers.background())
                        .map { response ->
                            if (response.isSuccessful) {
                                Result<VoteReceipt, VoteError>(VoteReceipt(response.body!!.transaction_id))
                            } else {
                                Result(VoteError(response.errorBody!!))
                            }
                        }.onErrorReturn {
                            Result(VoteError(it.message!!))
                        }
                }.onErrorReturn {
                    Result(VoteError(it.message!!))
                }
            }
    }
}