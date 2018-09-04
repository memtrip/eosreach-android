package com.memtrip.eosreach.api.actions

import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result
import io.reactivex.Single

interface AccountActionsRequest {

    fun getActionsForAccountName(
        contractName: String,
        accountName: String,
        position: Int = 0,
        offset: Int = 200
    ): Single<Result<AccountActionList, AccountActionsError>>
}

sealed class AccountActionsError : ApiError {
    object Generic : AccountActionsError()
}