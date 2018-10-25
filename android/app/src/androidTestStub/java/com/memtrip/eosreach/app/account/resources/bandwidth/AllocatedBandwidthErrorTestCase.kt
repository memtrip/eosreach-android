package com.memtrip.eosreach.app.account.resources.bandwidth

import com.memtrip.eosreach.R
import com.memtrip.eosreach.StubTestCase
import com.memtrip.eosreach.api.StubApi
import com.memtrip.eosreach.api.stub.Stub
import com.memtrip.eosreach.api.stub.StubMatcher
import com.memtrip.eosreach.api.stub.request.BasicStubRequest
import com.memtrip.eosreach.api.stub.request.ChainedStubRequest

class AllocatedBandwidthErrorTestCase : StubTestCase() {

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
            .verifyErrorAllocatedBandwidth()
            .selectErrorRetryAllocatedBandwidth()
            .verifyAllocatedBandwidthRow()
            .selectFirstAllocatedBandwidthListItem()
            .enterNetBalance("0.9000", R.id.account_resources_undelegate_bandwidth_fragment)
            .enterCpuBalance("1.1034", R.id.account_resources_undelegate_bandwidth_fragment)
            .selectBandwidthFormCtaButton(R.id.account_resources_undelegate_bandwidth_fragment)
            .verifyBandwidthConfirmScreen()
        bandwidthRobot
            .verifyBandwidthConfirmNetBalance("0.9000 SYS (\$971.21)")
            .verifyBandwidthConfirmCpuBalance("1.1034 SYS (\$1190.71)")
            .selectBandwidthConfirmButton()
        transactionRobot
            .selectViewLogButton()
            .verifyTransactionLog(readJsonFile("stub/error/error_push_transaction_log.json"))
        commonRobot
            .pressBack()
        bandwidthRobot
            .verifyBandwidthConfirmScreen()
    }

    override fun stubApi(): StubApi = object : StubApi(context()) {

        private val allocatedBandwidthRequest = ChainedStubRequest(BasicStubRequest(400))
            .next(BasicStubRequest(200, {
                readJsonFile("stub/happypath/happy_path_get_table_rows_delegated_bandwidth.json")
            }))

        override fun getTableRowsAllocatedBandwidth(): Stub = Stub(
            StubMatcher(
                context.getString(R.string.app_default_eos_endpoint_root),
                Regex("v1/chain/get_table_rows$"),
                readJsonFile("stub/request/request_get_table_rows_delegated_bandwidth.json")
            ),
            allocatedBandwidthRequest
        )
    }
}