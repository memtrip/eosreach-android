package com.memtrip.eosreach.app.account.resources.manage.manageram

import com.memtrip.mxandroid.MxViewIntent

sealed class ManageRamIntent : MxViewIntent {
    object BuyRamTabIdle : ManageRamIntent()
    object SellRamTabIdle : ManageRamIntent()
    data class Init(val symbol: String) : ManageRamIntent()
}