package com.memtrip.eosreach

import android.app.Application

import androidx.test.InstrumentationRegistry.getInstrumentation

import com.memtrip.eosreach.api.ApiConfig
import com.memtrip.eosreach.api.StubApi
import com.memtrip.eosreach.api.stub.StubInterceptor
import com.memtrip.eosreach.app.DaggerEosReachApplicationComponent
import com.memtrip.eosreach.billing.BillingConfig
import com.memtrip.eosreach.billing.BillingConnectionResponse

import java.util.Arrays.asList

class EntryActivityTestRule : BaseActivityTestRule() {

    private lateinit var stubApi: StubApi
    private lateinit var response: BillingConnectionResponse

    fun launch(
        stubApi: StubApi,
        response: BillingConnectionResponse
    ) {
        this.stubApi = stubApi
        this.response = response
        launch()
    }

    override fun inject() {
        val injector = DaggerEosReachApplicationComponent
            .builder()
            .application(getInstrumentation().targetContext.applicationContext as Application)
            .apiConfig(ApiConfig(asList(StubInterceptor(stubApi))))
            .billingConfig(BillingConfig(response))
            .build()

        TestStubEosReachApplication.setInjector(injector)
    }

    override fun resetInjector() {
        TestStubEosReachApplication.resetInjector()
    }
}