package com.memtrip.eosreach.api.stub.request

import okhttp3.Request
import okhttp3.Response

interface StubRequest {
    fun call(request: Request): Response
}