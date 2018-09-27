package com.memtrip.eosreach.api.stub.request

import okhttp3.Request
import okhttp3.Response

import java.util.LinkedList

class ChainedStubRequest(first: StubRequest) : StubRequest {

    private val requests: LinkedList<StubRequest> = with (LinkedList<StubRequest>()) {
        add(first)
        this
    }

    fun next(request: StubRequest): ChainedStubRequest = apply { requests.addLast(request) }

    override fun call(request: Request): Response = requests.removeFirst().call(request)
}