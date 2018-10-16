package com.memtrip.eosreach.app.transfer

import com.memtrip.eosreach.StubTestCase

class NavigateToAccountFromTransferTestCase : StubTestCase() {

    override fun test() {
        importKeyOrchestra.go("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3")
        accountRobot
            .verifyAccountScreen()
        balanceRobot
            .verifyBalanceScreen()
            .selectFirstTokenRow()
        actionsRobot
            .verifyActionsScreen()
            .selectTransferButton()
        transferRobot
            .verifyTransferScreen()
            .enterRecipient("memtripblock")
            .enterAmount("0.0001")
            .enterMemo("Here is some coin")
            .selectNextButton()
            .verifyTransferConfirmationScreen()
            .selectToAccountLabel()
        accountRobot
            .verifyReadOnlyAccountScreen()
    }
}