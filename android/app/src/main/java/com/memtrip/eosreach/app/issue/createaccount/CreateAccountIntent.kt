package com.memtrip.eosreach.app.issue.createaccount

import com.memtrip.eosreach.billing.BillingRequest

import com.memtrip.mxandroid.MxViewIntent

sealed class CreateAccountIntent : MxViewIntent {
    data class Init(val billingRequest: BillingRequest) : CreateAccountIntent()
    data class SetupGooglePlayBilling(val billingRequest: BillingRequest) : CreateAccountIntent()
    data class CreateAccount(
        val purchaseToken: String,
        val accountName: String
    ) : CreateAccountIntent()
    data class Done(val privateKey: String) : CreateAccountIntent()
}