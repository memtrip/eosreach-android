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
package com.memtrip.eosreach.api.ramprice

import com.memtrip.eos.chain.actions.query.ramprice.GetRamPrice
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.app.price.BalanceFormatter
import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

class RamPriceRequestImpl @Inject internal constructor(
    private val getRamPrice: GetRamPrice,
    private val rxSchedulers: RxSchedulers
) : RamPriceRequest {

    override fun getRamPrice(symbol: String): Single<Balance> {
        return getRamPrice.getPricePerKilobyte().map { price ->
            val formattedPrice = with (DecimalFormat("0.00000000")) {
                roundingMode = RoundingMode.CEILING
                this
            }.format(price)
            BalanceFormatter.deserialize("$formattedPrice $symbol")
        }.observeOn(rxSchedulers.main()).subscribeOn(rxSchedulers.background())
    }
}