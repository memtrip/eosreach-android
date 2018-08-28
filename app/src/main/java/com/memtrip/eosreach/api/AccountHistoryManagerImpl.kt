package com.memtrip.eosreach.api

import com.memtrip.eos.http.rpc.HistoryApi
import javax.inject.Inject

internal class AccountHistoryManagerImpl @Inject internal constructor(
    private val historyApi: HistoryApi
) : AccountHistoryManager {

}