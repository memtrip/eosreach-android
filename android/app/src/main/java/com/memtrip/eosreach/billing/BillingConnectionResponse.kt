package com.memtrip.eosreach.billing

import com.android.billingclient.api.SkuDetails

sealed class BillingConnectionResponse {
    data class Success(val skuDetails: SkuDetails) : BillingConnectionResponse()
    object SkuNotFound : BillingConnectionResponse()
    object SkuBillingUnavailable : BillingConnectionResponse()
    object SkuNotAvailable : BillingConnectionResponse()
    object SkuRequestFailed : BillingConnectionResponse()
    object BillingSetupFailed : BillingConnectionResponse()
}