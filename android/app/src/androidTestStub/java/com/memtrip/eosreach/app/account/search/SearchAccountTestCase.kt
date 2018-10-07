package com.memtrip.eosreach.app.account.search

import com.memtrip.eosreach.StubTestCase

class SearchAccountTestCase : StubTestCase() {

    override fun test() {
        importKeyOrchestra
            .go()
        accountRobot
            .verifyAccountScreen()
            .verifyAvailableBalance()
            .selectSearchMenuItem()
        searchRobot
            .verifySearchScreen()
            .typeAccountName("memtripissue")
            .selectAccount()
        accountRobot
            .verifyAccountScreen()
            .verifyAvailableBalance()
    }
}