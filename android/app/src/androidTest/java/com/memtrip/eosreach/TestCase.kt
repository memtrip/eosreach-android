package com.memtrip.eosreach

import com.memtrip.eosreach.robot.CommonRobot
import com.memtrip.eosreach.robot.account.AccountRobot
import com.memtrip.eosreach.robot.account.actions.ActionsRobot
import com.memtrip.eosreach.robot.account.balance.BalanceRobot
import com.memtrip.eosreach.robot.account.vote.VoteRobot
import com.memtrip.eosreach.robot.accountlist.AccountListRobot
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
    internal val accountListRobot = AccountListRobot()
    internal val accountRobot = AccountRobot()
    internal val balanceRobot = BalanceRobot()
    internal val voteRobot = VoteRobot()
    internal val actionsRobot = ActionsRobot()
    internal val transferRobot = TransferRobot()
    internal val transactionRobot = TransactionRobot()
    internal val settingsRobot = SettingsRobot()
}