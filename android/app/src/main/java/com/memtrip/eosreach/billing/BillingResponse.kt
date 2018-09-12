package com.memtrip.eosreach.billing

import com.android.billingclient.api.Purchase

data class BillingResponse(
    val purchase: Purchase?,
    val error: String? = null,
    val success: Boolean = (purchase != null && error == null)
)