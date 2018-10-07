package com.memtrip.eosreach.app.settings.viewprivatekeys

import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eosreach.R
import com.memtrip.eosreach.StubTestCase
import com.memtrip.eosreach.api.StubApi
import com.memtrip.eosreach.api.stub.Stub
import com.memtrip.eosreach.api.stub.StubMatcher
import com.memtrip.eosreach.api.stub.request.BasicStubRequest

class ViewSinglePrivateKeysTestCase : StubTestCase() {

    override fun test() {
        importKeyOrchestra.go("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3")
        accountNavigationRobot
            .selectNavigationIcon()
            .selectSettingsNavigationItem()
        settingsRobot
            .verifySettingsScreen()
            .selectViewPrivateKeysSettingsItem()
            .verifyViewPrivateKeysScreen()
            .selectShowPrivateKeysButton()

        val privateKey = EosPrivateKey("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3")

        settingsRobot
            .verifyViewPrivateKeyScreenPrivateKeyValue(privateKey.toString())
            .verifyViewPrivateKeyScreenPublicKeyValue(privateKey.publicKey.toString())
            .verifyViewPrivateKeyScreenAccountsValue("memtripissue")
    }

    override fun stubApi(): StubApi = object : StubApi(context()) {

        override fun getKeyAccounts(): Stub = Stub(
            StubMatcher(
                context.getString(R.string.app_default_eos_endpoint_root),
                Regex("v1/history/get_key_accounts$")
            ),
            BasicStubRequest(200, {
                readJsonFile("stub/happypath/happy_path_get_key_accounts_single.json")
            })
        )
    }
}