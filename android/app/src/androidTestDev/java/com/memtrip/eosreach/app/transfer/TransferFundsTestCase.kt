package com.memtrip.eosreach.app.transfer

import com.memtrip.eosreach.DevTestCase
import com.memtrip.eosreach.app.Config

class TransferFundsTestCase : DevTestCase() {

    override fun test() {
        splashRobot.navigateImportKey()
        importKeyRobot
            .typePrivateKey(Config.PRIVATE_KEY)
            .selectImportButton()
        accountListRobot
            .selectFirstAccountRow()
        accountRobot
            .verifyAccountSuccess()
        balanceRobot
            .verifyBalanceScreen()
            .selectFirstTokenRow()
        actionsRobot
            .selectTransferButton()

        val to = "memtripissue"
        val from = "memtripadmin"
        val amount = "0.0001"
        val memo = "Here is some coin, from android integration test."

        transferRobot
            .enterRecipient(to)
            .enterAmount(amount)
            .enterMemo(memo)
            .selectNextButton()
            .verifyAmount("$amount SYS")
            .verifyTo(to)
            .verifyFrom(from)
            .verifyMemo(memo)
            .selectConfirmButton()

        transactionRobot
            .selectDoneButton()
    }
}