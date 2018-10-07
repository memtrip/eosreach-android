package com.memtrip.eosreach.ochestra

class ImportKeyOrchestra : Orchestra() {

    fun go(privateKey: String) {
        splashRobot
            .selectImportKey()
        importKeyRobot
            .typePrivateKey(privateKey)
            .selectImportButton()
    }
}