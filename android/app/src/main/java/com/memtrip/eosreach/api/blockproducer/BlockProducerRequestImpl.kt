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
package com.memtrip.eosreach.api.blockproducer

import com.memtrip.eos.chain.actions.query.producer.GetBlockProducers

import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.utils.RxSchedulers

import io.reactivex.Single
import javax.inject.Inject

class BlockProducerRequestImpl @Inject internal constructor(
    private val blockProducers: GetBlockProducers,
    private val rxSchedulers: RxSchedulers
) : BlockProducerRequest {

    override fun getBlockProducers(limit: Int): Single<Result<List<BlockProducerDetails>, BlockProducerError>> {
        return blockProducers.getProducers(limit)
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
            .map { blockProducers ->
                Result<List<BlockProducerDetails>, BlockProducerError>(blockProducers.map { blockProducer ->
                    BlockProducerDetails(
                        blockProducer.bpJson.producer_account_name,
                        blockProducer.bpJson.org.candidate_name,
                        blockProducer.apiEndpoint,
                        blockProducer.bpJson.org.website,
                        blockProducer.bpJson.org.code_of_conduct,
                        blockProducer.bpJson.org.ownership_disclosure,
                        blockProducer.bpJson.org.email,
                        blockProducer.bpJson.org.branding.logo_256
                    )
                })
            }
            .onErrorReturn {
                Result(BlockProducerError.GenericError)
            }
    }

    override fun getSingleBlockProducer(accountName: String): Single<Result<BlockProducerDetails, BlockProducerError>> {
        return blockProducers.getSingleProducer(accountName)
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
            .map { blockProducer ->
                Result<BlockProducerDetails, BlockProducerError>(
                    BlockProducerDetails(
                        blockProducer.bpJson.producer_account_name,
                        blockProducer.bpJson.org.candidate_name,
                        blockProducer.apiEndpoint,
                        blockProducer.bpJson.org.website,
                        blockProducer.bpJson.org.code_of_conduct,
                        blockProducer.bpJson.org.ownership_disclosure,
                        blockProducer.bpJson.org.email,
                        blockProducer.bpJson.org.branding.logo_256
                    )
                )
            }
            .onErrorReturn { throwable ->
                if (throwable is GetBlockProducers.OnChainProducerJsonMissing) {
                    Result(BlockProducerError.OnChainProducerJsonMissing)
                } else {
                    Result(BlockProducerError.GenericError)
                }
            }
    }
}