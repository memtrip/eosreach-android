package com.memtrip.eosreach.api.eosprice

import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class EosPriceRequestImpl @Inject internal constructor(
    private val eosPriceApi: EosPriceApi,
    private val rxSchedulers: RxSchedulers
) : EosPriceRequest {

    override fun getPrice(currencyCode: String): Single<Result<EosPrice, EosPriceError>> {
        return eosPriceApi.getPrice(currencyCode)
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.main())
            .map {
                if (it.isSuccessful) {
                    Result(EosPrice(it.body()!!.value, it.body()!!.currency))
                } else {
                    if (it.code() == 406) {
                        Result<EosPrice, EosPriceError>(EosPriceError.UnsupportedCurrency)
                    } else {
                        Result<EosPrice, EosPriceError>(EosPriceError.Generic)
                    }
                }
            }

    }
}