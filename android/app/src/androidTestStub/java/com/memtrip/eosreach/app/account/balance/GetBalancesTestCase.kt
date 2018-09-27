package com.memtrip.eosreach.app.account.balance

import com.memtrip.eosreach.StubTestCase

class GetBalancesTestCase : StubTestCase() {

    override fun test() {
        splashRobot.navigateImportKey()
        importKeyRobot
            .typePrivateKey("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3")
            .selectImportButton()
        accountListRobot
            .verifyAccountListScreen()
            .selectFirstAccountRow()
        accountRobot
            .verifyAccountSuccess()
        balanceRobot
            .verifyBalanceScreen()
            .verifyTokenTitle()
            .verifySysBalanceRow()
    }
}