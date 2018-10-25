package com.memtrip.eosreach.app.actions

import com.memtrip.eosreach.StubTestCase

class NavigateToAccountFromActionTestCase : StubTestCase() {

    override fun test() {
        importKeyOrchestra.go("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3")
        accountRobot
            .verifyAccountScreen()
        balanceRobot
            .verifyBalanceScreen()
            .selectFirstTokenRow()
        actionsRobot
            .verifyActionsScreen()
            .selectFirstActionRow()
            .verifyViewTransferActionScreen()
            .selectViewTransferActionFromAccountLabel()
        accountRobot
            .verifyReadOnlyAccountScreen()
        balanceRobot
            .verifyBalanceScreen()
            .selectFirstTokenRow()
        actionsRobot
            .verifyActionsReadOnlyScreen()
            .selectFirstActionRow()
            .verifyViewTransferActionScreen()
            .selectViewTransferActionFromAccountLabel()
            // the label will be read only at the point, so the user will remain on the current screen
            .verifyViewTransferActionScreen()
    }
}