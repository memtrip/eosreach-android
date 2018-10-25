/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.memtrip.eosreach.billing

import android.app.Activity
import android.app.Application

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams

import com.android.billingclient.api.SkuDetailsParams
import com.memtrip.eosreach.R

import javax.inject.Inject

class BillingImpl @Inject constructor(
    application: Application
) : Billing {

    private val skuId: String = application.getString(R.string.app_default_create_account_sku_id)

    lateinit var billingFlowResponse: (response: BillingFlowResponse) -> Unit
    lateinit var billingConnectionResponse: (response: BillingConnectionResponse) -> Unit

    private val billingClient: BillingClient = BillingClient
        .newBuilder(application)
        .setListener { responseCode, purchases ->
            if (responseCode == BillingClient.BillingResponse.OK && purchases != null && purchases.isNotEmpty()) {
                billingFlowResponse(BillingFlowResponse.Success(purchases[0].purchaseToken))
            } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
                billingFlowResponse(BillingFlowResponse.UserCancelledFlow)
            } else if (responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
                billingFlowResponse(BillingFlowResponse.ItemAlreadyOwned)
            } else {
                billingFlowResponse(BillingFlowResponse.Error)
            }
        }
        .build()

    override fun startConnection(
        billingFlowResponse: (response: BillingFlowResponse) -> Unit,
        billingConnectionResponse: (response: BillingConnectionResponse) -> Unit
    ) {
        this.billingFlowResponse = billingFlowResponse
        this.billingConnectionResponse = billingConnectionResponse

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(responseCode: Int) {
                if (responseCode == BillingClient.BillingResponse.OK) {
                    billingClient.querySkuDetailsAsync(with(SkuDetailsParams.newBuilder()) {
                        setSkusList(with(ArrayList<String>()) {
                            add(skuId)
                            this
                        })
                        setType(BillingClient.SkuType.INAPP)
                        this
                    }.build()) { skuResponseCode, skuDetailsList ->
                        if (skuResponseCode == BillingClient.BillingResponse.OK) {
                            if (skuDetailsList.isNotEmpty()) {
                                billingConnectionResponse(BillingConnectionResponse.Success(skuDetailsList[0]))
                            } else {
                                billingConnectionResponse(BillingConnectionResponse.SkuNotFound)
                            }
                        } else if (skuResponseCode == BillingClient.BillingResponse.BILLING_UNAVAILABLE) {
                            billingConnectionResponse(BillingConnectionResponse.SkuBillingUnavailable)
                        } else if (skuResponseCode == BillingClient.BillingResponse.ITEM_UNAVAILABLE) {
                            billingConnectionResponse(BillingConnectionResponse.SkuNotAvailable)
                        } else {
                            billingConnectionResponse(BillingConnectionResponse.SkuRequestFailed)
                        }
                    }
                } else {
                    billingConnectionResponse(BillingConnectionResponse.BillingSetupFailed)
                }
            }
            override fun onBillingServiceDisconnected() { }
        })
    }

    override fun launchBillingFlow(
        skuId: String,
        activity: Activity
    ) {
        billingClient.launchBillingFlow(
            activity,
            BillingFlowParams.newBuilder()
                .setSku(skuId)
                .setType(BillingClient.SkuType.INAPP)
                .build()
        )
    }

    override fun consumeItem(purchaseToken: String, purchaseConsumed: () -> Unit) {
        billingClient.consumeAsync(purchaseToken) { _, _ ->
            purchaseConsumed()
        }
    }

    override fun endConnection() {
        billingClient.endConnection()
    }
}