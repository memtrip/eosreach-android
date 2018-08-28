package com.memtrip.eosreach.app.welcome.createaccount

import com.memtrip.mxandroid.MxViewIntent

sealed class CreateAccountIntent : MxViewIntent {
    object Init : CreateAccountIntent()
    data class CreateAccount(val accountName: String) : CreateAccountIntent()
}