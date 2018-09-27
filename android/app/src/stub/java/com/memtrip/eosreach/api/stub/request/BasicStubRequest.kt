package com.memtrip.eosreach.api.stub.request

import com.memtrip.eosreach.api.stub.create
import okhttp3.Headers
import okhttp3.Request
import okhttp3.Response

class BasicStubRequest(
    private val code: Int,
    private val body: () -> String = { "" },
    private val headers: Headers = Headers.of()
) : StubRequest {
    override fun call(request: Request): Response {
        return Response.Builder().create(
            request,
            code,
            body.invoke(),
            headers)
    }
}