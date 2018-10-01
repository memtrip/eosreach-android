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