package com.memtrip.eosreach.api.stub

import com.memtrip.eosreach.api.StubApi
import okhttp3.Interceptor
import okhttp3.Response

class StubInterceptor(
    private val stubApi: StubApi
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return StubConnection(chain.request(), stubApi).performRequest()
    }
}