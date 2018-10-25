package com.memtrip.eosreach.app.account.search

import com.memtrip.eosreach.StubTestCase

class VerifyReadOnlySearchAccountTestCase : StubTestCase() {

    override fun test() {
        splashRobot
            .selectExplore()
        searchRobot
            .verifySearchScreen()
            .typeAccountName("memtripissue")
            .selectAccount()
        accountRobot
            .verifyReadOnlyAccountScreen()
            .verifyAvailableBalance()
            .selectVoteTab()
        voteRobot
            .verifyVoteReadOnly()
        accountRobot
            .selectResourcesTab()
        resourcesRobot
            .verifyResourcesReadOnly()
        accountRobot
            .selectBalanceTab()
        balanceRobot
            .selectFirstTokenRow()
        actionsRobot
            .verifyActionsReadOnlyScreen()
    }
}