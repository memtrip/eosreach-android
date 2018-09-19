package com.memtrip.eosreach.api.eoscreateaccount

import androidx.annotation.Keep
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

@Keep
@JsonClass(generateAdapter = true)
data class CreateAccountRequest(
    val purchaseToken: String,
    val accountName: String,
    val publicKey: String
)

@Keep
@JsonClass(generateAdapter = true)
data class CreateAccountResponse(
    val transactionId: String
)

@Keep
@JsonClass(generateAdapter = true)
data class CreateAccountError(
    val errorCode: String
)