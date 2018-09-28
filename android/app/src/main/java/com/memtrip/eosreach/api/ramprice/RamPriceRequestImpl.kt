package com.memtrip.eosreach.api.ramprice

import com.memtrip.eos.chain.actions.query.ramprice.GetRamPrice
import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.model.contract.request.GetTableRows
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.price.BalanceFormatter
import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import java.io.IOException
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