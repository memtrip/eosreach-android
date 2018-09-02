package com.memtrip.eosreach.api.stub.response

import okhttp3.Headers
import okhttp3.Request
import okhttp3.Response

fun Response.Builder.create(request: Request, code: Int): Response {
    return this.create(request, code, "{}")
}

fun Response.Builder.create(request: Request, code: Int, body: String): Response {
    return this.create(request, code, body, Headers.Builder().build())
}

fun Response.Builder.create(request: Request, code: Int, body: String, headers: Headers): Response {
    this.request(request)
    this.protocol(okhttp3.Protocol.HTTP_2)
    this.message("stub")
    this.code(code)
    this.body(okhttp3.ResponseBody.create(null, body))
    this.headers(headers)
    return this.build()
}