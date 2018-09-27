package com.memtrip.eosreach.app

import com.memtrip.eosreach.api.ApiConfig
import com.memtrip.eosreach.api.HappyPathStubApi
import com.memtrip.eosreach.api.stub.StubInterceptor
import com.memtrip.eosreach.billing.BillingConfig
import com.memtrip.eosreach.billing.BillingConnectionResponse
import java.util.Arrays.asList

class EosReachApplication : BaseEosReachApplication() {

    override val daggerEosReachApplicationComponent: EosReachApplicationComponent by lazy {
        DaggerEosReachApplicationComponent
            .builder()
            .application(this)
            .apiConfig(ApiConfig(asList(StubInterceptor(HappyPathStubApi(this)))))
            .billingConfig(BillingConfig(BillingConnectionResponse.BillingSetupFailed))
            .build()
    }
}