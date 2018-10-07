package com.memtrip.eosreach.app.account.search

import com.memtrip.eosreach.R
import com.memtrip.eosreach.StubTestCase
import com.memtrip.eosreach.api.StubApi
import com.memtrip.eosreach.api.stub.Stub
import com.memtrip.eosreach.api.stub.StubMatcher
import com.memtrip.eosreach.api.stub.request.BasicStubRequest
import com.memtrip.eosreach.api.stub.request.ChainedStubRequest

class SearchAccountErrorTestCase : StubTestCase() {

    override fun test() {
        splashRobot
            .selectExplore()
        searchRobot
            .verifySearchScreen()
            .typeAccountName("memtripissue")
            .verifySearchError()
        searchRobot
            .selectSearchErrorRetry()
            .selectAccount()
        accountRobot
            .verifyAccountScreen()
            .verifyAvailableBalance()
    }

    override fun stubApi(): StubApi = object : StubApi(context()) {

        val getAccountsSuccess = BasicStubRequest(200, {
            readJsonFile("stub/happypath/happy_path_get_account_unstaked.json")
        })

        val requestChain = ChainedStubRequest(BasicStubRequest(400))
            .next(getAccountsSuccess)
            .next(getAccountsSuccess)

        override fun getAccount(): Stub = Stub(
            StubMatcher(
                context.getString(R.string.app_default_eos_endpoint_root),
                Regex("v1/chain/get_account$")
            ),
            requestChain
        )
    }
}