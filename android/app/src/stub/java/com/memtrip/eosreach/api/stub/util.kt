package com.memtrip.eosreach.api.stub

import android.content.Context
import okhttp3.Headers
import okhttp3.Request
import okhttp3.Response
import okio.Buffer

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

fun bodyToString(request: Request): String {
    val copy = request.newBuilder().build()
    val buffer = Buffer()
    copy.body()!!.writeTo(buffer)
    return buffer.readUtf8()
}

fun readFile(fileName: String, context: Context): String {
    return context.assets.open(fileName).bufferedReader().use {
        it.readText()
    }
}