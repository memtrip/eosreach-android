package com.memtrip.eosreach.app

import com.memtrip.eosreach.api.ApiConfig

class EosReachApplication : BaseEosReachApplication() {

    override val daggerEosReachApplicationComponent: EosReachApplicationComponent by lazy {
        DaggerEosReachApplicationComponent
            .builder()
            .application(this)
            .apiConfig(ApiConfig())
            .build()
    }
}