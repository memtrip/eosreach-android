package com.memtrip.eosreach

import com.memtrip.eosreach.api.ApiConfig
import com.memtrip.eosreach.api.HappyPathStubApi
import com.memtrip.eosreach.api.stub.StubInterceptor
import com.memtrip.eosreach.app.BaseEosReachApplication
import com.memtrip.eosreach.app.DaggerEosReachApplicationComponent

import com.memtrip.eosreach.app.EosReachApplicationComponent
import dagger.android.AndroidInjector
import java.util.Arrays

class TestStubEosReachApplication : BaseEosReachApplication() {

    override fun onCreate() {
        instance = this
        super.onCreate()
    }

    private fun inject() {
        applicationInjector().inject(this)
    }

    override fun applicationInjector(): AndroidInjector<BaseEosReachApplication> {
        return androidInjector ?: super.applicationInjector()
    }

    override val daggerEosReachApplicationComponent: EosReachApplicationComponent by lazy {
        DaggerEosReachApplicationComponent
            .builder()
            .application(this)
            .apiConfig(ApiConfig(Arrays.asList(StubInterceptor(HappyPathStubApi(this)))))
            .build()
    }

    companion object {

        lateinit var instance: TestStubEosReachApplication
        private var androidInjector: AndroidInjector<BaseEosReachApplication>? = null

        fun setInjector(injector: AndroidInjector<BaseEosReachApplication>) {
            androidInjector = injector
            instance.inject()
        }

        fun resetInjector() {
            androidInjector = null
            instance.inject()
        }
    }
}