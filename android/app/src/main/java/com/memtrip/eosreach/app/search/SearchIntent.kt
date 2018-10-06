package com.memtrip.eosreach.app.search

import com.memtrip.eosreach.db.account.AccountEntity
import com.memtrip.mxandroid.MxViewIntent

sealed class SearchIntent : MxViewIntent {
    object Idle : SearchIntent()
    object Init : SearchIntent()
    data class SearchValueChanged(val searchValue: String) : SearchIntent()
    data class ViewAccount(val accountEntity: AccountEntity) : SearchIntent()
}