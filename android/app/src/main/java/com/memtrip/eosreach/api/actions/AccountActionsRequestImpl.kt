/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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
            it.printStackTrace()
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
        }.distinctBy { historicAccountAction ->
            historicAccountAction.action_trace.trx_id
        }

        return if (historicActions.isNotEmpty()) {
            Result(AccountActionList(historicActions.reversed().map { createAccountAction(contractAccountBalance, it) }))
        } else {
            if (historicAccountActionParent.actions.isNotEmpty()) {
                Result(AccountActionList(emptyList(), historicAccountActionParent.actions.last().account_action_seq))
            } else {
                Result(AccountActionList(emptyList()))
            }
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