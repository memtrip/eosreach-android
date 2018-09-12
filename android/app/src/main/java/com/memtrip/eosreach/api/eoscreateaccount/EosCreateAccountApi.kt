package com.memtrip.eosreach.api.eoscreateaccount

import com.squareup.moshi.JsonClass
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface EosCreateAccountApi {

    @POST("createAccount")
    fun createAccount(
        @Body createAccountRequest: CreateAccountRequest
    ): Single<Response<CreateAccountResponse>>
}

@JsonClass(generateAdapter = true)
data class CreateAccountRequest(
    val purchaseToken: String,
    val accountName: String,
    val publicKey: String
)

@JsonClass(generateAdapter = true)
data class CreateAccountResponse(
    val transactionId: String
)

@JsonClass(generateAdapter = true)
data class CreateAccountError(
    val error: String
)