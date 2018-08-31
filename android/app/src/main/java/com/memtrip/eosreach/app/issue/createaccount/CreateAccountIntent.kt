package com.memtrip.eosreach.app.issue.createaccount

import com.memtrip.mxandroid.MxViewIntent

sealed class CreateAccountIntent : MxViewIntent {
    object Init : CreateAccountIntent()
    data class CreateAccount(val accountName: String) : CreateAccountIntent()
}