package com.memtrip.eosreach.app.account.search

import com.memtrip.eosreach.StubTestCase

class SearchAccountTestCase : StubTestCase() {

    override fun test() {
        importKeyOrchestra.go("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3")
        accountRobot
            .verifyAccountScreen()
            .verifyAvailableBalance()
            .selectSearchMenuItem()
        searchRobot
            .verifySearchScreen()
            .typeAccountName("memtripissue")
            .selectAccount()
        accountRobot
            .verifyReadOnlyAccountScreen()
            .verifyAvailableBalance()
    }
}