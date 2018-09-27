package com.memtrip.eosreach.billing

import android.app.Activity

interface Billing {

    fun startConnection(
        billingFlowResponse: (response: BillingFlowResponse) -> Unit,
        billingConnectionResponse: (response: BillingConnectionResponse) -> Unit
    )

    fun launchBillingFlow(skuId: String, activity: Activity)

    fun consumeItem(purchaseToken: String, purchaseConsumed: () -> Unit)

    fun endConnection()
}