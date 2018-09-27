package com.memtrip.eosreach.app.accountlist

import com.memtrip.eosreach.StubTestCase

class RefreshAccountListTestCase : StubTestCase() {

    override fun test() {
        splashRobot.navigateImportKey()
        importKeyRobot
            .typePrivateKey("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3")
            .selectImportButton()
        accountListRobot
            .verifyAccountListScreen()
            .verifyFirstAccountRow()
            .verifySecondAccountRow()
            .selectRefreshButton()
            .verifyFirstAccountRow()
            .verifySecondAccountRow()
    }
}