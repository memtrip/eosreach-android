package com.memtrip.eosreach.api.actions.model

import com.memtrip.eos.http.rpc.model.history.response.HistoricAccountAction
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.app.price.BalanceParser

class TransferAccountAction(
    accountName: String,
    action: HistoricAccountAction
) : AccountAction(accountName, action) {
    val from: String = actionData["from"].toString()
    val to: String = actionData["to"].toString()
    val quantity: Balance = BalanceParser.deserialize(actionData["quantity"].toString())
    val memo: String = actionData["memo"].toString()
}