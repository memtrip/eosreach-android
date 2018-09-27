package com.memtrip.eosreach.billing

import android.app.Activity
import javax.inject.Inject

class BillingStub @Inject constructor(
    private val billingConfig: BillingConfig?
) : Billing {

    private lateinit var billingResponse: (response: BillingFlowResponse) -> Unit

    override fun startConnection(
        billingResponse: (response: BillingFlowResponse) -> Unit,
        billingConnectionResponse: (response: BillingConnectionResponse) -> Unit
    ) {
        this.billingResponse = billingResponse

        billingConnectionResponse.invoke(billingConfig!!.response)
    }

    override fun launchBillingFlow(skuId: String, activity: Activity) {
        when (skuId) {
            SkuStub.SUCCESS.name -> {
                billingResponse(BillingFlowResponse.Success("purchaseToken"))
            }
            SkuStub.FLOW_CANCELLED.name -> {
                billingResponse(BillingFlowResponse.UserCancelledFlow)
            }
            SkuStub.ITEM_ALREADY_OWNED.name -> {
                billingResponse(BillingFlowResponse.ItemAlreadyOwned)
            }
            SkuStub.ERROR.name -> {
                billingResponse(BillingFlowResponse.Error)
            }
        }
    }

    override fun consumeItem(purchaseToken: String, purchaseConsumed: () -> Unit) {
        purchaseConsumed()
    }

    override fun endConnection() {
    }
}