package com.memtrip.eosreach.api.eosprice

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EosPriceApi {

    @GET("price/{currency}")
    fun getPrice(@Path("currency") currency: String): Single<Response<Price>>
}

@Keep
@JsonClass(generateAdapter = true)
data class Price(
    val value: Double,
    val currency: String
)