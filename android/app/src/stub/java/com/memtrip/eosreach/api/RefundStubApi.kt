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
package com.memtrip.eosreach.api

import android.content.Context
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.stub.Stub
import com.memtrip.eosreach.api.stub.StubMatcher
import com.memtrip.eosreach.api.stub.request.BasicStubRequest
import com.memtrip.eosreach.api.stub.request.ChainedStubRequest

/**
 * Used to test the refund error scenarios
 */
class RefundStubApi(context: Context) : StubApi(context) {

    private val refundRequest = ChainedStubRequest(BasicStubRequest(400, {
        readJsonFile("stub/error/error_push_transaction_log.json")
    })).next(BasicStubRequest(200, {
        readJsonFile("stub/happypath/happy_path_push_transaction_log.json")
    }))

    private val accountRequest = ChainedStubRequest(BasicStubRequest(200, {
        readJsonFile("stub/happypath/happy_path_get_account_refund.json")
    })).next(BasicStubRequest(200, {
        readJsonFile("stub/happypath/happy_path_get_account_staked.json")
    }))

    override fun pushTransaction(): Stub = Stub(
        StubMatcher(
            context.getString(R.string.app_default_eos_endpoint_root),
            Regex("v1/chain/push_transaction$")
        ),
        refundRequest
    )

    override fun getAccount(): Stub = Stub(
        StubMatcher(
            context.getString(R.string.app_default_eos_endpoint_root),
            Regex("v1/chain/get_account$")
        ),
        accountRequest
    )
}