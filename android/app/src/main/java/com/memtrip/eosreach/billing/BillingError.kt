package com.memtrip.eosreach.billing

sealed class BillingError : Exception() {
    object BillingSetupConnectionFailed : BillingError()
    object BillingSetupFailed : BillingError()
    object SkuBillingUnavailable : BillingError()
    object SkuNotFound : BillingError()
    object SkuNotAvailable : BillingError()
    object SkuAlreadyOwned : BillingError()
    object SkuRequestFailed : BillingError()
}