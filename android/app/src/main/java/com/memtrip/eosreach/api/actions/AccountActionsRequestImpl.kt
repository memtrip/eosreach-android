package com.memtrip.eosreach.api.actions

import com.memtrip.eos.http.rpc.HistoryApi

class AccountActionsRequestImpl(
    private val historyApi: HistoryApi
) : AccountActionsRequest {

    fun getActionsForAccountName(accountName: String) {
    }
}