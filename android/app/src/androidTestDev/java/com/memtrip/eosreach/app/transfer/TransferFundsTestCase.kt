/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.memtrip.eosreach.app.transfer

import com.memtrip.eosreach.DevTestCase
import com.memtrip.eosreach.app.Config

class TransferFundsTestCase : DevTestCase() {

    override fun test() {
        importKeyOrchestra.go(Config.PRIVATE_KEY)
        accountRobot
            .verifyAccountScreen()
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
            .verifyTransactionReceiptScreen()
            .selectDoneButton()

        actionsRobot
            .verifyActionsScreen()
    }
}