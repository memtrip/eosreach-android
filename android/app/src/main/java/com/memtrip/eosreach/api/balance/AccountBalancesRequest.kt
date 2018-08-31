package com.memtrip.eosreach.api.balance

import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result

import io.reactivex.Single
import okhttp3.ResponseBody

interface AccountBalanceRequest {

    fun getBalance(
        contractName: String,
        accountName: String
    ): Single<Result<AccountBalanceList, BalanceError>>
}

sealed class BalanceError : ApiError {
    object Generic : BalanceError()
    data class FailedRetrievingBalance(
        val code: Int,
        val body: ResponseBody?
    ) : BalanceError()
}