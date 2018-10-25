package com.memtrip.eosreach.app.account.resources.bandwidth

import com.memtrip.eosreach.R
import com.memtrip.eosreach.StubTestCase
import com.memtrip.eosreach.api.StubApi
import com.memtrip.eosreach.api.stub.Stub
import com.memtrip.eosreach.api.stub.StubMatcher
import com.memtrip.eosreach.api.stub.request.BasicStubRequest

class AllocatedBandwidthEmptyTestCase : StubTestCase() {

    override fun test() {
        importKeyOrchestra.go("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3")
        accountRobot
            .verifyAccountScreen()
            .selectResourcesTab()
        resourcesRobot
            .selectBandwidthButton()
        bandwidthRobot
            .verifyManageBandwidthScreen()
            .selectAllocatedTab()
            .verifyEmptyAllocatedBandwidth()
    }

    override fun stubApi(): StubApi = object : StubApi(context()) {

        override fun getTableRowsAllocatedBandwidth(): Stub = Stub(
            StubMatcher(
                context.getString(R.string.app_default_eos_endpoint_root),
                Regex("v1/chain/get_table_rows$"),
                readJsonFile("stub/request/request_get_table_rows_delegated_bandwidth.json")
            ),
            BasicStubRequest(200, {
                readJsonFile("stub/happypath/happy_path_get_table_rows_delegated_bandwidth_empty.json")
            })
        )
    }
}