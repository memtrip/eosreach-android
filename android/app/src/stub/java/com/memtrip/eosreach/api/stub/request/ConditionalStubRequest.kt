package com.memtrip.eosreach.api.stub.request

import okhttp3.Request
import okhttp3.Response

class ConditionalStubRequest(
    private val conditional: (request: Request) -> StubRequest
) : StubRequest {
    override fun call(request: Request): Response {
        return conditional.invoke(request).call(request)
    }
}