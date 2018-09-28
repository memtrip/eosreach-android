package com.memtrip.eosreach.app.account.vote

import com.memtrip.eosreach.StubTestCase

class NotVotedTestCase : StubTestCase() {

    override fun test() {
        splashRobot.navigateImportKey()
        importKeyRobot
            .typePrivateKey("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3")
            .selectImportButton()
        accountListRobot
            .selectFirstAccountRow()
        accountRobot
            .verifyAccountSuccess()
            .verifyAvailableBalance()
            .selectVoteTab()
        voteRobot
            .verifyNotVotedScreen()
    }


}