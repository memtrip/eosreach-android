package com.memtrip.eosreach.billing

import android.app.Activity
import android.app.Application
import android.content.Context
import com.android.billingclient.api.BillingClient

import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ConsumeResponseListener
import com.memtrip.eosreach.R
import com.memtrip.eosreach.db.sharedpreferences.UnusedBillingPurchaseId

class Billing(
    context: Context,
    billingResponse: (response: BillingResponse) -> Unit,
    private val unusedAccountPurchase: UnusedBillingPurchaseId = UnusedBillingPurchaseId(context.applicationContext as Application),
    val billingClient: BillingClient = BillingClient
        .newBuilder(context)
        .setListener { responseCode, purchases ->
            if (responseCode == BillingClient.BillingResponse.OK && purchases != null && purchases.isNotEmpty()) {
                unusedAccountPurchase.put(purchases[0].purchaseToken)
                billingResponse(BillingResponse(purchases[0].purchaseToken))
            } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
                billingResponse(BillingResponse(
                    null,
                    context.getString(R.string.issue_create_account_billing_purchase_cancelled_error)))
            } else if (responseCode == BillingClient.BillingResponse.ITEM_UNAVAILABLE) {
                billingResponse(BillingResponse(
                    null,
                    context.getString(R.string.issue_create_account_billing_purchase_item_unavailable_error)))
            } else if (responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
                val purchaseId = unusedAccountPurchase.get()
                if (purchaseId.isNotEmpty()) {
                    billingResponse(BillingResponse(unusedAccountPurchase.get()))
                } else {
                    // TODO: Ask the user to manually enter their purchaseId
                    billingResponse(BillingResponse(
                        null,
                        context.getString(R.string.issue_create_account_billing_purchase_fatal_error)))
                }
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