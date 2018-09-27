package com.memtrip.eosreach.app.welcome.importkey

import com.memtrip.eosreach.StubTestCase

class ImportInvalidKeyTestCase : StubTestCase() {

    override fun test() {
        splashRobot.navigateImportKey()
        importKeyRobot
            .verifyImportKeyScreen()
            .typePrivateKey("invalid_key")
            .selectImportButton()
            .verifyInvalidKeyError()
        commonRobot.clickDialogOk()
    }
}