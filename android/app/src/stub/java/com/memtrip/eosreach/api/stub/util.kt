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