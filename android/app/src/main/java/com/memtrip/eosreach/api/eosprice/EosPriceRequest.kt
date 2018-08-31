package com.memtrip.eosreach.api.eosprice

import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result
import io.reactivex.Single

interface EosPriceRequest {
    fun getPrice(currencyCode: String): Single<Result<EosPrice, EosPriceError>>
}

sealed class EosPriceError : ApiError {
    object Generic : EosPriceError()
    object UnsupportedCurrency : EosPriceError()
}