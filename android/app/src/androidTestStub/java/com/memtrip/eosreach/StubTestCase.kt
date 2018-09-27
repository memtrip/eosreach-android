package com.memtrip.eosreach

import android.content.Context
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.runner.AndroidJUnit4
import com.android.billingclient.api.SkuDetails

import com.memtrip.eosreach.api.HappyPathStubApi
import com.memtrip.eosreach.api.StubApi
import com.memtrip.eosreach.billing.BillingConnectionResponse
import com.memtrip.eosreach.billing.SkuStub

import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class StubTestCase : TestCase() {

    @Test
    fun run() {

        setup()

        rule.launch(stubApi(), billingConnectionResponse())

        assumeTrue(hasSupportedEnvironment(BuildConfig.FLAVOR))

        test()
    }

    open fun setup() {
    }

    abstract fun test()

    open fun stubApi(): StubApi {
        return HappyPathStubApi(getTargetContext())
    }

    open fun billingConnectionResponse(): BillingConnectionResponse {
        return BillingConnectionResponse.BillingSetupFailed
    }

    internal fun skuDetails(skuStub: SkuStub): SkuDetails {
        return SkuDetails(
            "{ \"productId\": \"${skuStub.name}\", \"price\": \"\$1.99\" }")
    }

    internal fun context(): Context {
        return getTargetContext()
    }

    private fun getSupportedEnvironments(): Array<String> = arrayOf("offline")

    private fun hasSupportedEnvironment(environment: String): Boolean {
        return getSupportedEnvironments().none { supportedEnvironment ->
            supportedEnvironment == environment
        }
    }
}
