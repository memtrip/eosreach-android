package com.memtrip.eosreach.app.issue.createaccount

import com.android.billingclient.api.SkuDetails
import com.memtrip.eosreach.billing.BillingError

import com.memtrip.mxandroid.MxViewIntent

sealed class CreateAccountIntent : MxViewIntent {
    object Init : CreateAccountIntent()
    data class BillingSetupSuccess(val skuDetails: SkuDetails) : CreateAccountIntent()
    data class BillingSetupFailed(val billingError: BillingError) : CreateAccountIntent()
    data class BuyAccount(val accountName: String) : CreateAccountIntent()
    data class CreateAccount(
        val purchaseToken: String,
        val accountName: String
    ) : CreateAccountIntent()
    data class Done(val privateKey: String) : CreateAccountIntent()
}