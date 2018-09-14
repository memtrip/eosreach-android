package com.memtrip.eosreach.app.account.resources.manage.manageram

import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.mxandroid.MxViewIntent

sealed class ManageRamIntent : MxViewIntent {
    object BuyRamTabIdle : ManageRamIntent()
    object SellRamTabIdle : ManageRamIntent()
    data class Init(
        val eosAccount: EosAccount
    ) : ManageRamIntent()
}