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

import com.memtrip.eosreach.api.StubApi
import okhttp3.Request
import okhttp3.Response

class StubConnection(
    private val request: Request,
    vararg stubApi: StubApi
) {

    private val stubs: List<Stub> = stubApi.flatMap { it.stubs() }

    fun performRequest(): Response {
        return match(request)
    }

    private fun match(request: Request): Response {
        return stubs
            .first { stub ->
                val urlMatch = stub.matcher.urlMatcher.containsMatchIn(request.url().toString())
                if (stub.matcher.bodyMatcher != null) {
                    val bodyMatch = stub.matcher.bodyMatcher == bodyToString(request)
                    urlMatch && bodyMatch
                } else {
                    urlMatch
                }
            }
            .stubRequest
            .call(request)
    }
}