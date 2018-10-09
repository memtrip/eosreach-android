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
package com.memtrip.eosreach

import com.memtrip.eosreach.ochestra.ImportKeyOrchestra
import com.memtrip.eosreach.robot.CommonRobot
import com.memtrip.eosreach.robot.account.AccountNavigationRobot
import com.memtrip.eosreach.robot.account.AccountRobot
import com.memtrip.eosreach.robot.account.actions.ActionsRobot
import com.memtrip.eosreach.robot.account.balance.BalanceRobot
import com.memtrip.eosreach.robot.account.resources.ResourcesRobot
import com.memtrip.eosreach.robot.account.search.SearchRobot
import com.memtrip.eosreach.robot.account.vote.VoteRobot
import com.memtrip.eosreach.robot.blockproducer.BlockProducerRobot
import com.memtrip.eosreach.robot.issue.CreateAccountRobot
import com.memtrip.eosreach.robot.issue.ImportKeyRobot
import com.memtrip.eosreach.robot.settings.SettingsRobot
import com.memtrip.eosreach.robot.transaction.TransactionRobot
import com.memtrip.eosreach.robot.transfer.TransferRobot
import com.memtrip.eosreach.robot.welcome.SplashRobot
import org.junit.Rule

abstract class TestCase {

    @get:Rule var rule = EntryActivityTestRule()

    internal val commonRobot = CommonRobot()
    internal val splashRobot = SplashRobot()
    internal val importKeyRobot = ImportKeyRobot()
    internal val createAccountRobot = CreateAccountRobot()
    internal val accountNavigationRobot = AccountNavigationRobot()
    internal val accountRobot = AccountRobot()
    internal val searchRobot = SearchRobot()
    internal val balanceRobot = BalanceRobot()
    internal val voteRobot = VoteRobot()
    internal val resourcesRobot = ResourcesRobot()
    internal val actionsRobot = ActionsRobot()
    internal val transferRobot = TransferRobot()
    internal val transactionRobot = TransactionRobot()
    internal val blockProducerRobot = BlockProducerRobot()
    internal val settingsRobot = SettingsRobot()

    internal val importKeyOrchestra = ImportKeyOrchestra()
}