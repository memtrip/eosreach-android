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
package com.memtrip.eosreach

import android.content.Context
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.runner.AndroidJUnit4
import com.android.billingclient.api.SkuDetails

import com.memtrip.eosreach.api.HappyPathStubApi
import com.memtrip.eosreach.api.StubApi
import com.memtrip.eosreach.api.stub.readFile
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

    internal fun readJsonFile(fileName: String): String {
        return readFile(fileName, getTargetContext())
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
