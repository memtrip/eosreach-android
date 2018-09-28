package com.memtrip.eosreach.app.account.vote

import android.os.SystemClock
import com.memtrip.eosreach.StubTestCase

class VoteForProxyTestCase : StubTestCase() {

    override fun test() {
        splashRobot
            .navigateImportKey()
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
            .selectVoteForProxy()
            .verifyCastProxyVoteScreen()
            .typeCastProxyVote("memtripproxy")
            .selectCastProxyVoteButton()
        transactionRobot
            .verifyTransactionErrorDialog()
            .selectViewLogButton()
            .verifyTransactionLog(readJsonFile("stub/error/error_push_transaction_log.json"))
        commonRobot.pressBack()
        voteRobot
            .verifyCastProxyVoteInputValue("memtripproxy")
    }
}