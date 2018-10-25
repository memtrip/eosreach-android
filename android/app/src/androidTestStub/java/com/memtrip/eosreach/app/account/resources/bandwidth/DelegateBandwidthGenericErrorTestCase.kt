package com.memtrip.eosreach.app.account.resources.bandwidth

import com.memtrip.eosreach.R
import com.memtrip.eosreach.StubTestCase
import com.memtrip.eosreach.api.StubApi
import com.memtrip.eosreach.api.stub.Stub
import com.memtrip.eosreach.api.stub.StubMatcher
import com.memtrip.eosreach.api.stub.request.BasicStubRequest
import java.net.SocketTimeoutException

class DelegateBandwidthGenericErrorTestCase : StubTestCase() {

    override fun test() {
        importKeyOrchestra.go("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3")
        accountRobot
            .verifyAccountScreen()
            .selectResourcesTab()
        resourcesRobot
            .selectBandwidthButton()
        bandwidthRobot
            .verifyManageBandwidthScreen()
            .enterNetBalance("0.9000", R.id.account_resources_delegate_bandwidth_fragment)
            .enterCpuBalance("1.1034", R.id.account_resources_delegate_bandwidth_fragment)
            .selectBandwidthFormCtaButton(R.id.account_resources_delegate_bandwidth_fragment)
            .verifyBandwidthConfirmScreen()
            .verifyBandwidthConfirmNetBalance("0.9000 SYS (\$971.21)")
            .verifyBandwidthConfirmCpuBalance("1.1034 SYS (\$1190.71)")
            .selectBandwidthConfirmButton()
            .verifyBandwidthConfirmGenericError()
            .selectBandwidthConfirmGenericErrorPositiveButton()
            .verifyBandwidthConfirmScreen()
    }

    override fun stubApi(): StubApi = object : StubApi(context()) {

        override fun pushTransaction(): Stub = Stub(
            StubMatcher(
                context.getString(R.string.app_default_eos_endpoint_root),
                Regex("v1/chain/push_transaction$")
            ),
            BasicStubRequest(-999, { throw SocketTimeoutException() })
        )
    }
}