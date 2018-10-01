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
package com.memtrip.eosreach.app.issue.createaccount

import com.android.billingclient.api.SkuDetails
import com.memtrip.eosreach.billing.BillingError
import com.memtrip.eosreach.billing.BillingFlowResponse

import com.memtrip.mxandroid.MxViewIntent

sealed class CreateAccountIntent : MxViewIntent {
    object Idle : CreateAccountIntent()
    object Init : CreateAccountIntent()
    object StartBillingConnection : CreateAccountIntent()
    data class BillingSetupSuccess(val skuDetails: SkuDetails) : CreateAccountIntent()
    data class BillingSetupFailed(val billingError: BillingError) : CreateAccountIntent()
    data class BillingFlowComplete(
        val accountName: String,
        val billingResponse: BillingFlowResponse
    ) : CreateAccountIntent()
    data class BuyAccount(val accountName: String) : CreateAccountIntent()
    data class CreateAccount(
        val purchaseToken: String,
        val accountName: String
    ) : CreateAccountIntent()
    data class CreateAccountError(val error: String) : CreateAccountIntent()
    object RetryLimbo : CreateAccountIntent()
    object Done : CreateAccountIntent()
}