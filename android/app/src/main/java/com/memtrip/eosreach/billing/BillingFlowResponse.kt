package com.memtrip.eosreach.billing

sealed class BillingFlowResponse {
    data class Success(val purchaseToken: String) : BillingFlowResponse()
    object UserCancelledFlow : BillingFlowResponse()
    object ItemAlreadyOwned : BillingFlowResponse()
    object Error : BillingFlowResponse()
}