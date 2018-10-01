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
package com.memtrip.eosreach.api.vote

import com.memtrip.eos.chain.actions.transaction.TransactionContext
import com.memtrip.eos.chain.actions.transaction.vote.VoteChain

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
    private val voteChain: VoteChain,
    private val rxSchedulers: RxSchedulers
) : VoteRequest {

    override fun voteForProducer(
        voterAccountName: String,
        producers: List<String>
    ): Single<Result<VoteReceipt, VoteError>> {
        return getAccountByName.select(voterAccountName).flatMap { accountEntity ->
            eosKeyManager.getPrivateKey(accountEntity.publicKey).flatMap { eosPrivateKey ->
                voteChain.vote(VoteChain.Args(
                    voterAccountName,
                    "",
                    producers
                ), TransactionContext(
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
                        it.printStackTrace()
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
                    voteChain.vote(VoteChain.Args(
                        voterAccountName,
                        proxyVoteAccountName,
                        emptyList()
                    ), TransactionContext(
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