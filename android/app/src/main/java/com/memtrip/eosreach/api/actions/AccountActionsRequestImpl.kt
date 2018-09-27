package com.memtrip.eosreach.api.actions

import com.memtrip.eos.http.rpc.HistoryApi
import com.memtrip.eos.http.rpc.model.history.request.GetActions
import com.memtrip.eos.http.rpc.model.history.response.HistoricAccountAction
import com.memtrip.eos.http.rpc.model.history.response.HistoricAccountActionParent
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.actions.model.AccountAction
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.utils.RxSchedulers

import io.reactivex.Single
import javax.inject.Inject

class AccountActionsRequestImpl @Inject internal constructor(
    private val historyApi: HistoryApi,
    private val rxSchedulers: RxSchedulers
) : AccountActionsRequest {

    override fun getActionsForAccountName(
        contractAccountBalance: ContractAccountBalance,
        position: Int,
        offset: Int
    ): Single<Result<AccountActionList, AccountActionsError>> {
        return historyApi.getActions(GetActions(contractAccountBalance.accountName, position, offset)).map { response ->
            if (response.isSuccessful) {
                filterActionsForAccountName(contractAccountBalance, response.body()!!)
            } else {
                Result<AccountActionList, AccountActionsError>(AccountActionsError.Generic)
            }
        }.onErrorReturn {
            Result(AccountActionsError.Generic)
        }.subscribeOn(rxSchedulers.background()).observeOn(rxSchedulers.main())
    }

    private fun filterActionsForAccountName(
        contractAccountBalance: ContractAccountBalance,
        historicAccountActionParent: HistoricAccountActionParent
    ): Result<AccountActionList, AccountActionsError> {
        val historicActions = historicAccountActionParent.actions.filter {
            it.action_trace.act.account == contractAccountBalance.contractName &&
                it.action_trace.act.name == "transfer"
        }

        return if (historicActions.isNotEmpty()) {
            Result(AccountActionList(historicActions.reversed().map { createAccountAction(contractAccountBalance, it) }))
        } else {
            Result(AccountActionList(emptyList()))
        }
    }

    private fun createAccountAction(
        contractAccountBalance: ContractAccountBalance,
        action: HistoricAccountAction
    ): AccountAction = when (action.action_trace.act.name) {
        "transfer" -> AccountAction.createTransfer(action, contractAccountBalance)
        else -> throw IllegalStateException("transfer is currently the only supported action type.")
    }
}