package com.memtrip.eosreach.api.ramprice

import com.memtrip.eosreach.api.balance.Balance
import io.reactivex.Single

interface RamPriceRequest {
    fun getRamPrice(symbol: String): Single<Balance>
}