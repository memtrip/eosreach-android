package com.memtrip.eosreach.api.stub.request

import okhttp3.Headers
import okhttp3.Request
import okhttp3.Response

import java.util.LinkedList

class ErrorOnFirstStubRequest(
    private val code: Int,
    private val body: () -> String = { "" },
    private val headers: Headers = Headers.of()
) : StubRequest {

    private val requests: LinkedList<StubRequest> = with (LinkedList<StubRequest>()) {
        add(BasicStubRequest(400))
        this
    }

    override fun call(request: Request): Response {
        return with (requests.removeFirst().call(request)) {
            requests.add(BasicStubRequest(code, body, headers))
            this
        }
    }
}