package com.memtrip.eosreach.app.welcome.importkey

import com.memtrip.eosreach.StubTestCase

class ImportKeyHappyPathTestCase : StubTestCase() {

    override fun test() {
        splashRobot.navigateImportKey()
        importKeyRobot
            .typePrivateKey("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3")
            .selectImportButton()
    }
}