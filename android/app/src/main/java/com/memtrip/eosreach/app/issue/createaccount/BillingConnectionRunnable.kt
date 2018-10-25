package com.memtrip.eosreach.app.issue.createaccount

import com.memtrip.eosreach.billing.BillingConnectionResponse
import com.memtrip.eosreach.billing.BillingError
import java.lang.ref.WeakReference

class BillingConnectionRunnable(
    model: CreateAccountViewModel,
    private val response: BillingConnectionResponse,
    private val createAccountViewModel: WeakReference<CreateAccountViewModel> = WeakReference(model)
) : Runnable {

    override fun run() {
        createAccountViewModel.get()?.let { model ->
            handleBillingConnection(model, response)
        }
    }

    private fun handleBillingConnection(
        createAccountViewModel: CreateAccountViewModel,
        response: BillingConnectionResponse
    ): Unit = when (response) {
        is BillingConnectionResponse.Success -> {
            createAccountViewModel.publish(CreateAccountIntent.BillingSetupSuccess(response.skuDetails))
        }
        BillingConnectionResponse.SkuNotFound -> {
            createAccountViewModel.publish(CreateAccountIntent.BillingSetupFailed(BillingError.SkuNotFound))
        }
        BillingConnectionResponse.SkuBillingUnavailable -> {
            createAccountViewModel.publish(CreateAccountIntent.BillingSetupFailed(BillingError.SkuBillingUnavailable))
        }
        BillingConnectionResponse.SkuNotAvailable -> {
            createAccountViewModel.publish(CreateAccountIntent.BillingSetupFailed(BillingError.SkuNotAvailable))
        }
        BillingConnectionResponse.SkuRequestFailed -> {
            createAccountViewModel.publish(CreateAccountIntent.BillingSetupFailed(BillingError.SkuRequestFailed))
        }
        BillingConnectionResponse.BillingSetupFailed -> {
            createAccountViewModel.publish(CreateAccountIntent.BillingSetupFailed(BillingError.BillingSetupFailed))
        }
    }
}