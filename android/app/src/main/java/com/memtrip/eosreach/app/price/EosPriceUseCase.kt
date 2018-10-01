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
package com.memtrip.eosreach.app.price

import com.memtrip.eosreach.api.eosprice.EosPrice
import com.memtrip.eosreach.api.eosprice.EosPriceRequest

import com.memtrip.eosreach.db.sharedpreferences.EosPriceCurrencyPair
import com.memtrip.eosreach.db.sharedpreferences.EosPriceLastUpdated
import com.memtrip.eosreach.db.sharedpreferences.EosPriceValue
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
                                eosPriceCurrencyPair.get())
                        } else {
                            EosPrice.unavailable()
                        }
                    }
                }
        }
    }

    fun refreshPrice(currencyCode: String): Single<EosPrice> {
        return eosPriceRequest.getPrice(currencyCode)
            .map { response ->
                if (response.success) {
                    eosPriceLastUpdated.clear()
                    eosPriceCurrencyPair.put(response.data!!.currency)
                    eosPriceValue.put(response.data.value.toFloat())
                    response.data
                } else {
                    EosPrice.unavailable()
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