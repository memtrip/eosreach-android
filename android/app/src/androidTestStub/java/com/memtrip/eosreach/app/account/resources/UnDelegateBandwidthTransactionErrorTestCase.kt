package com.memtrip.eosreach.app.account.resources

import com.memtrip.eosreach.R
import com.memtrip.eosreach.StubTestCase

class UnDelegateBandwidthTransactionErrorTestCase : StubTestCase() {

    override fun test() {
        importKeyOrchestra
            .go()
        accountRobot
            .verifyAccountScreen()
            .selectResourcesTab()
        resourcesRobot
            .selectBandwidthButton()
            .verifyManageBandwidthScreen()
            .selectUndelegateTab()
            .enterNetBalance("0.9000", R.id.account_resources_undelegate_bandwidth_fragment)
            .enterCpuBalance("1.1034", R.id.account_resources_undelegate_bandwidth_fragment)
            .selectBandwidthFormCtaButton(R.id.account_resources_undelegate_bandwidth_fragment)
            .verifyBandwidthConfirmScreen()
            .verifyBandwidthConfirmNetBalance("0.9000 SYS (\$971.21)")
            .verifyBandwidthConfirmCpuBalance("1.1034 SYS (\$1190.71)")
            .selectBandwidthConfirmButton()
        transactionRobot
            .selectViewLogButton()
            .verifyTransactionLog(readJsonFile("stub/error/error_push_transaction_log.json"))
        commonRobot
            .pressBack()
        resourcesRobot
            .verifyBandwidthConfirmScreen()
    }
}