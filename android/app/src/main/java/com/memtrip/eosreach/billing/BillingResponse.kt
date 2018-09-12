package com.memtrip.eosreach.billing

data class BillingResponse(
    val purchaseToken: String?,
    val error: String? = null,
    val success: Boolean = (purchaseToken != null && error == null)
)