package com.memtrip.eosreach.app.account

import com.memtrip.eosreach.R
import com.memtrip.eosreach.StubTestCase
import com.memtrip.eosreach.api.StubApi
import com.memtrip.eosreach.api.stub.Stub
import com.memtrip.eosreach.api.stub.StubMatcher
import com.memtrip.eosreach.api.stub.request.BasicStubRequest
import com.memtrip.eosreach.api.stub.request.ChainedStubRequest

class SwipeRefreshAccountErrorTestCase : StubTestCase() {

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
            .swipeToRefresh()
            .showErrorDialog()
    }

    override fun stubApi(): StubApi = object : StubApi(context()) {

        private val request = ChainedStubRequest(
            BasicStubRequest(200, {
                readJsonFile("stub/happypath/happy_path_get_account_unstaked.json")
            })
        ).next(
            BasicStubRequest(400)
        )

        override fun getAccount(): Stub = Stub(
            StubMatcher(
                context.getString(R.string.app_default_eos_endpoint_root),
                Regex("v1/chain/get_account$")
            ),
            request
        )
    }
}