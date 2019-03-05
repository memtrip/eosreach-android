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
package com.memtrip.eosreach.app.resources

import com.memtrip.eosreach.DevTestCase
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.Config

class SellRamTestCase : DevTestCase() {

    override fun test() {
        importKeyOrchestra.go(Config.PRIVATE_KEY)
        accountRobot
            .verifyAccountScreen()
            .selectResourcesTab()
        resourcesRobot
            .verifyResourcesScreen()
            .selectRamButton()
        ramRobot
            .verifyManageRamScreen()
            .selectSellTab()
            .enterRamAmount("100", R.id.account_resources_manage_ram_sell_fragment)
            .selectCtaButton(R.id.account_resources_manage_ram_sell_fragment)
            .verifyConfirmSellTitle()
            .verifyConfirmKb("100")
            .verifyPrice()
            .selectConfirmCta()

        transactionRobot
            .verifyTransactionReceiptScreen()
            .selectDoneButton()

        resourcesRobot
            .verifyResourcesScreen()
    }
}