package com.memtrip.eosreach.app.account

import com.memtrip.eosreach.R
import com.memtrip.eosreach.StubTestCase
import com.memtrip.eosreach.api.StubApi
import com.memtrip.eosreach.api.stub.Stub
import com.memtrip.eosreach.api.stub.StubMatcher
import com.memtrip.eosreach.api.stub.request.BasicStubRequest

class GetAccountPriceErrorTestCase : StubTestCase() {

    override fun test() {
        splashRobot.navigateImportKey()
        importKeyRobot
            .typePrivateKey("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3")
            .selectImportButton()
        accountListRobot
            .verifyAccountListScreen()
            .verifyFirstAccountRow()
            .verifySecondAccountRow()
            .selectFirstAccountRow()
        accountRobot
            .verifyAccountSuccess()
            .verifyUnavailableBalance()
    }

    override fun stubApi(): StubApi = object : StubApi(context()) {

        override fun getPriceForCurrency(): Stub = Stub(
            StubMatcher(
                context.getString(R.string.app_default_utility_endpoint_root),
                Regex("price/(.*)$")
            ),
            BasicStubRequest(400)
        )
    }
}