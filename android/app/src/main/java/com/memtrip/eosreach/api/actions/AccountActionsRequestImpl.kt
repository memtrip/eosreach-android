package com.memtrip.eosreach.api.actions

import com.memtrip.eos.http.rpc.HistoryApi
import com.memtrip.eos.http.rpc.model.history.request.GetActions
import com.memtrip.eos.http.rpc.model.history.response.HistoricAccountActionParent
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.actions.model.AccountAction
import com.memtrip.eosreach.utils.RxSchedulers

import io.reactivex.Single
import javax.inject.Inject

class AccountActionsRequestImpl @Inject internal constructor(
    private val historyApi: HistoryApi,
    private val rxSchedulers: RxSchedulers
) : AccountActionsRequest {

    override fun getActionsForAccountName(
        contractName: String,
        accountName: String,
        position: Int,
        offset: Int
    ): Single<Result<AccountActionList, AccountActionsError>> {
        return historyApi.getActions(GetActions(accountName, position, offset)).map { response ->
            if (response.isSuccessful) {
                filterActionsForAccountName(contractName, accountName, response.body()!!)
            } else {
                Result<AccountActionList, AccountActionsError>(AccountActionsError.Generic)
            }
        }.subscribeOn(rxSchedulers.background()).observeOn(rxSchedulers.main())
    }

    private fun filterActionsForAccountName(
        contractName: String,
        accountName: String,
        historicAccountActionParent: HistoricAccountActionParent
    ): Result<AccountActionList, AccountActionsError> {
        val historicActions = historicAccountActionParent.actions.filter {
            it.action_trace.act.account == contractName ||
                it.action_trace.receipt.receiver == contractName
        }

        return if (historicActions.isNotEmpty()) {
            Result(AccountActionList(historicActions.map { AccountAction.create(accountName, it) }))
        } else {
            Result(AccountActionsError.NoResults)
        }
    }
}