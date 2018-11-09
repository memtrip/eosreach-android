package com.memtrip.eosreach.api.blockproducer

import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.model.producer.request.GetProducers
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class RegisteredBlockProducerRequestImpl @Inject constructor(
    private val chainApi: ChainApi,
    private val rxSchedulers: RxSchedulers
) : RegisteredBlockProducerRequest {

    override fun getProducers(limit: Int, lowerLimit: String): Single<Result<List<RegisteredBlockProducer>, RegisteredBlockProducerError>> {
        return chainApi.getProducers(GetProducers(
            true,
            lowerLimit,
            limit
        )).observeOn(rxSchedulers.main()).subscribeOn(rxSchedulers.background()).map { response ->
            if (response.isSuccessful) {
                val results = response.body()!!.rows
                if (results.isNotEmpty()) {
                    if (lowerLimit != "") {
                        results.drop(1)
                    } else {
                        results.drop(1)
                    }
                    Result(dropFirstItemWhenPaginationResult(results.map { producer ->
                        RegisteredBlockProducer(
                            producer.owner,
                            producer.total_votes,
                            producer.producer_key,
                            producer.is_active,
                            producer.url
                        )
                    }, lowerLimit))
                } else {
                    Result<List<RegisteredBlockProducer>, RegisteredBlockProducerError>(RegisteredBlockProducerError.Empty)
                }
            } else {
                Result<List<RegisteredBlockProducer>, RegisteredBlockProducerError>(RegisteredBlockProducerError.GenericError)
            }
        }.onErrorReturn {
            Result(RegisteredBlockProducerError.GenericError)
        }
    }

    private fun dropFirstItemWhenPaginationResult(
        results: List<RegisteredBlockProducer>, lowerLimit: String
    ): List<RegisteredBlockProducer> {
        return if (lowerLimit.isNotEmpty()) {
            results.drop(1)
        } else {
            results
        }
    }
}