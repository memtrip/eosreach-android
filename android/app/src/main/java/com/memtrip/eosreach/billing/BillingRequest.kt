package com.memtrip.eosreach.billing

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import io.reactivex.Single

class BillingRequest(
    val skuId: String,
    val billingClient: BillingClient
) {

    fun getCreateAccountSku(): Single<SkuDetails> {
        return Single.create<SkuDetails> { single ->
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(responseCode: Int) {
                    if (responseCode == BillingClient.BillingResponse.OK) {
                        billingClient.querySkuDetailsAsync(with (SkuDetailsParams.newBuilder()) {
                            setSkusList(with (ArrayList<String>()) {
                                add(skuId)
                                this
                            })
                            setType(BillingClient.SkuType.INAPP)
                            this
                        }.build()) { skuResponseCode, skuDetailsList ->
                            if (skuResponseCode == BillingClient.BillingResponse.OK) {
                                if (skuDetailsList.isNotEmpty()) {
                                    single.onSuccess(skuDetailsList[0])
                                } else {
                                    single.onError(BillingError.SkuNotFound)
                                }
                            } else if (skuResponseCode == BillingClient.BillingResponse.BILLING_UNAVAILABLE) {
                                single.onError(BillingError.SkuBillingUnavailable)
                            } else if (skuResponseCode == BillingClient.BillingResponse.ITEM_UNAVAILABLE) {
                                single.onError(BillingError.SkuNotAvailable)
                            } else if (skuResponseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
                                single.onError(BillingError.SkuAlreadyOwned)
                            } else {
                                single.onError(BillingError.SkuRequestFailed)
                            }
                        }
                    } else {
                        single.onError(BillingError.BillingSetupFailed)
                    }
                }
                override fun onBillingServiceDisconnected() {
                    if (!single.isDisposed) {
                        single.onError(BillingError.BillingSetupConnectionFailed)
                    }
                }
            })
        }
    }
}