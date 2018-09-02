package com.memtrip.eosreach.app

import com.memtrip.eosreach.api.ApiConfig
import com.memtrip.eosreach.api.HappyPathStubApi
import com.memtrip.eosreach.api.stub.StubInterceptor
import java.util.Arrays.asList

class EosReachApplication : BaseEosReachApplication() {

    override val daggerEosReachApplicationComponent: EosReachApplicationComponent by lazy {
        DaggerEosReachApplicationComponent
            .builder()
            .application(this)
            .apiConfig(ApiConfig(asList(StubInterceptor(HappyPathStubApi(this)))))
            .build()
    }
}