package com.memtrip.eosreach.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient

import com.android.billingclient.api.BillingFlowParams
import com.memtrip.eosreach.R
import com.memtrip.eosreach.db.sharedpreferences.UnusedBillingPurchaseId

class Billing(
    context: Context,
    billingResponse: (response: BillingResponse) -> Unit,
    private val unusedAccountPurchase: UnusedBillingPurchaseId = UnusedBillingPurchaseId(context),
    val billingClient: BillingClient = BillingClient
        .newBuilder(context)
        .setListener { responseCode, purchases ->
            if (responseCode == BillingClient.BillingResponse.OK && purchases != null && purchases.isNotEmpty()) {
                unusedAccountPurchase.put(purchases[0].purchaseToken)
                billingResponse(BillingResponse(purchases[0].purchaseToken))
            } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
                billingResponse(BillingResponse(
                    null,
                    context.getString(R.string.issue_create_account_billing_purchase_failed_error)))
            } else if (responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
                billingResponse(BillingResponse(unusedAccountPurchase.get()))
            } else {
                billingResponse(BillingResponse(
                    null,
                    context.getString(R.string.issue_create_account_billing_purchase_failed_error)))
            }
        }
        .build()
) {

    fun launchBillingFlow(skuId: String, activity: Activity) {
        billingClient.launchBillingFlow(
            activity,
            BillingFlowParams.newBuilder()
                .setSku(skuId)
                .setType(BillingClient.SkuType.INAPP)
                .build()
        )
    }
}