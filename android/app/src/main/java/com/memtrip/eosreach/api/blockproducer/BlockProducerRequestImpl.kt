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

import com.memtrip.eos.chain.actions.query.producer.BlockProducer
import com.memtrip.eos.chain.actions.query.producer.GetBlockProducersAggregate

import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.utils.RxSchedulers

import io.reactivex.Single
import javax.inject.Inject

class BlockProducerRequestImpl @Inject internal constructor(
    private val blockProducersAggregate: GetBlockProducersAggregate,
    private val rxSchedulers: RxSchedulers
) : BlockProducerRequest {

    override fun getBlockProducers(limit: Int): Single<Result<List<BlockProducer>, BlockProducerError>> {
        return blockProducersAggregate.getProducers(limit)
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
            .map {
                Result<List<BlockProducer>, BlockProducerError>(it)
            }
            .onErrorReturn {
                Result(BlockProducerError())
            }
    }
}