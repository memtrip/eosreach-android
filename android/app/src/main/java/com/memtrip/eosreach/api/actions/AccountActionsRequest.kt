package com.memtrip.eosreach.api.actions

import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import io.reactivex.Single

interface AccountActionsRequest {

    fun getActionsForAccountName(
        contractAccountBalance: ContractAccountBalance,
        position: Int = -1,
        offset: Int = -50
    ): Single<Result<AccountActionList, AccountActionsError>>
}

sealed class AccountActionsError : ApiError {
    object Generic : AccountActionsError()
}