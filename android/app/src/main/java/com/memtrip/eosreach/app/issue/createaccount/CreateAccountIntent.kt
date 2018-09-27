package com.memtrip.eosreach.app.issue.createaccount

import com.android.billingclient.api.SkuDetails
import com.memtrip.eosreach.billing.BillingError
import com.memtrip.eosreach.billing.BillingFlowResponse

import com.memtrip.mxandroid.MxViewIntent

sealed class CreateAccountIntent : MxViewIntent {
    object Idle : CreateAccountIntent()
    object Init : CreateAccountIntent()
    object StartBillingConnection : CreateAccountIntent()
    data class BillingSetupSuccess(val skuDetails: SkuDetails) : CreateAccountIntent()
    data class BillingSetupFailed(val billingError: BillingError) : CreateAccountIntent()
    data class BillingFlowComplete(
        val accountName: String,
        val billingResponse: BillingFlowResponse
    ) : CreateAccountIntent()
    data class BuyAccount(val accountName: String) : CreateAccountIntent()
    data class CreateAccount(
        val purchaseToken: String,
        val accountName: String
    ) : CreateAccountIntent()
    data class CreateAccountError(val error: String) : CreateAccountIntent()
    object RetryLimbo : CreateAccountIntent()
    object Done : CreateAccountIntent()
}