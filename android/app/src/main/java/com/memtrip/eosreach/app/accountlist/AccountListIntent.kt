package com.memtrip.eosreach.app.accountlist

import com.memtrip.eosreach.db.AccountEntity
import com.memtrip.mxandroid.MxViewIntent

sealed class AccountListIntent : MxViewIntent {
    object Init : AccountListIntent()
    object Idle : AccountListIntent()
    data class AccountSelected(val accountName: AccountEntity) : AccountListIntent()
}