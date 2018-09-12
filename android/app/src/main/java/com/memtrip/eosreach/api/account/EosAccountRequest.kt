package com.memtrip.eosreach.api.account

import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result

import io.reactivex.Single
import okhttp3.ResponseBody

interface EosAccountRequest {

    fun getAccount(accountName: String): Single<Result<EosAccount, AccountError>>
}

sealed class AccountError : ApiError {
    data class FailedRetrievingAccount(
        val code: Int,
        val body: ResponseBody?
    ) : AccountError()
}