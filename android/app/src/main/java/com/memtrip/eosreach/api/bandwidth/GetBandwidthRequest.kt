package com.memtrip.eosreach.api.bandwidth

import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result
import io.reactivex.Single

interface GetBandwidthRequest {
    fun getBandwidth(accountName: String): Single<Result<List<DelegatedBandwidth>, GetBandwidthError>>
}

sealed class GetBandwidthError : ApiError {
    object Empty : GetBandwidthError()
    object GenericError : GetBandwidthError()
}