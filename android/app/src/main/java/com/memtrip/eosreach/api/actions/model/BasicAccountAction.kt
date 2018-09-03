package com.memtrip.eosreach.api.actions.model

import com.memtrip.eos.http.rpc.model.history.response.HistoricAccountAction

class BasicAccountAction(
    accountName: String,
    action: HistoricAccountAction
) : AccountAction(accountName, action)