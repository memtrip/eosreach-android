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
package com.memtrip.eosreach.app.accountlist

import com.memtrip.eosreach.R
import com.memtrip.eosreach.StubTestCase
import com.memtrip.eosreach.api.StubApi
import com.memtrip.eosreach.api.stub.Stub
import com.memtrip.eosreach.api.stub.StubMatcher
import com.memtrip.eosreach.api.stub.request.BasicStubRequest
import com.memtrip.eosreach.api.stub.request.ChainedStubRequest

class AccountListErrorTestCase : StubTestCase() {

    override fun test() {
        importKeyOrchestra.go("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3")
        accountNavigationRobot
            .selectNavigationIcon()
            .verifyAccountNavigationScreen()
            .verifyFirstAccountRow()
            .verifySecondAccountRow()
            .selectRefreshButton()
            .verifyAccountListError()
            .selectAccountErrorRetry()
            .verifyFirstAccountRow()
            .verifySecondAccountRow()
    }

    override fun stubApi(): StubApi = object : StubApi(context()) {

        val getKeyAccounts = BasicStubRequest(200, {
            readJsonFile("stub/happypath/happy_path_get_key_accounts.json")
        })

        val chainedRequest = ChainedStubRequest(getKeyAccounts)
            .next(BasicStubRequest(400))
            .next(getKeyAccounts)

        override fun getKeyAccounts(): Stub = Stub(
            StubMatcher(
                context.getString(R.string.app_default_eos_endpoint_root),
                Regex("v1/history/get_key_accounts$")
            ),
            chainedRequest
        )
    }
}