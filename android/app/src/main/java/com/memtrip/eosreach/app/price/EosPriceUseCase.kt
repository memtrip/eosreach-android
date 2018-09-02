package com.memtrip.eosreach.app.price

import com.memtrip.eosreach.api.eosprice.EosPrice
import com.memtrip.eosreach.api.eosprice.EosPriceRequest

import com.memtrip.eosreach.db.EosPriceCurrencyPair
import com.memtrip.eosreach.db.EosPriceLastUpdated
import com.memtrip.eosreach.db.EosPriceValue
import io.reactivex.Single
import javax.inject.Inject

class EosPriceUseCase @Inject constructor(
    private val eosPriceValue: EosPriceValue,
    private val eosPriceCurrencyPair: EosPriceCurrencyPair,
    private val eosPriceLastUpdated: EosPriceLastUpdated,
    private val eosPriceRequest: EosPriceRequest
) {

    fun getPrice(): Single<EosPrice> {
        if (eosPriceLastUpdated.exists() && !expired(eosPriceLastUpdated.get())) {
            return Single.just(EosPrice(
                eosPriceValue.get().toDouble(),
                eosPriceCurrencyPair.get()))
        } else {
            return eosPriceRequest.getPrice(eosPriceCurrencyPair.get())
                .map {
                    if (it.success) {
                        eosPriceLastUpdated.put(System.currentTimeMillis())
                        it.data
                    } else {
                        if (eosPriceLastUpdated.exists()) {
                            EosPrice(
                                eosPriceValue.get().toDouble(),
                                eosPriceCurrencyPair.get(),
                                true)
                        } else {
                            EosPrice.unavailable()
                        }
                    }
                }
        }
    }

    private fun expired(lastUpdated: Long): Boolean {
        return lastUpdated > (System.currentTimeMillis() - TEN_MINUTES)
    }

    companion object {
        private const val TEN_MINUTES = 600000
    }
}