package com.memtrip.eosreach.api.bandwidth

import com.memtrip.eos.chain.actions.query.bandwidth.GetDelegatedBandwidth
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class GetBandwidthRequestImpl @Inject internal constructor(
    private val getDelegatedBandwidth: GetDelegatedBandwidth,
    private val rxSchedulers: RxSchedulers
) : GetBandwidthRequest {

    override fun getBandwidth(accountName: String): Single<Result<List<DelegatedBandwidth>, GetBandwidthError>> {
        return getDelegatedBandwidth.getBandwidth(accountName)
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background()).map { result ->
                if (result.isNotEmpty()) {
                    Result(result.map { bandwidthJson ->
                        DelegatedBandwidth(
                            bandwidthJson.to,
                            bandwidthJson.net_weight,
                            bandwidthJson.cpu_weight)
                    })
                } else {
                    Result<List<DelegatedBandwidth>, GetBandwidthError>(GetBandwidthError.Empty)
                }
            }.onErrorReturn {
                it.printStackTrace()
                Result(GetBandwidthError.GenericError)
            }
    }
}