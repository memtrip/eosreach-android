package com.memtrip.eosreach.app.voterproxy

import com.memtrip.eosreach.R
import com.memtrip.eosreach.StubTestCase
import com.memtrip.eosreach.api.StubApi
import com.memtrip.eosreach.api.stub.Stub
import com.memtrip.eosreach.api.stub.StubMatcher
import com.memtrip.eosreach.api.stub.request.BasicStubRequest
import com.memtrip.eosreach.api.stub.request.ChainedStubRequest

class VoterProxyListErrorTestCase : StubTestCase() {

    override fun test() {
        importKeyOrchestra.go("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3")
        accountRobot
            .verifyAccountScreen()
            .verifyAvailableBalance()
            .selectVoteTab()
        voteRobot
            .verifyNotVotedScreen()
            .selectVoteForProxy()
            .verifyCastProxyVoteScreen()
            .selectExploreProxyAccountsButton()
        proxyVoterRobot
            .verifyProxyVoterListErrorScreen()
            .selectProxyVoterListRetryButton()
            .verifyProxyVoterListScreen()
            .selectProxyVoterListFirstAccountRow()
        voteRobot
            .verifyCastProxyVoteScreen()
            .verifyCastProxyVoteInputValue("aagileproxyy")
    }

    override fun stubApi(): StubApi = object : StubApi(context()) {

        private val blockProducerRequest = ChainedStubRequest(BasicStubRequest(400))
            .next(BasicStubRequest(200, {
                readJsonFile("stub/happypath/happy_path_get_table_rows_regproxyinfo.json")
            }))

        override fun getTableRowsProxyVoter(): Stub = Stub(
            StubMatcher(
                context.getString(R.string.app_default_eos_endpoint_root),
                Regex("v1/chain/get_table_rows$"),
                readJsonFile("stub/request/request_get_table_rows_regproxyinfo.json")
            ),
            blockProducerRequest
        )
    }
}