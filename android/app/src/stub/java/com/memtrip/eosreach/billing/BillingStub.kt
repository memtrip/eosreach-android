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