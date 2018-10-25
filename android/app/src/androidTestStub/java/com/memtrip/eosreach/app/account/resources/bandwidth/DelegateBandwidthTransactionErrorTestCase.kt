package com.memtrip.eosreach.app.account.resources.bandwidth

import com.memtrip.eosreach.R
import com.memtrip.eosreach.StubTestCase

class DelegateBandwidthTransactionErrorTestCase : StubTestCase() {

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
        transactionRobot
            .selectViewLogButton()
            .verifyTransactionLog(readJsonFile("stub/error/error_push_transaction_log.json"))
        commonRobot
            .pressBack()
        bandwidthRobot
            .verifyBandwidthConfirmScreen()
    }
}