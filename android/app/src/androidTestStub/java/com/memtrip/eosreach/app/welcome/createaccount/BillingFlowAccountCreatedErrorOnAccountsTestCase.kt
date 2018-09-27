package com.memtrip.eosreach.app.welcome.createaccount

import com.memtrip.eosreach.R
import com.memtrip.eosreach.StubTestCase
import com.memtrip.eosreach.api.StubApi
import com.memtrip.eosreach.api.stub.Stub
import com.memtrip.eosreach.api.stub.StubMatcher
import com.memtrip.eosreach.api.stub.request.ErrorOnFirstStubRequest
import com.memtrip.eosreach.billing.BillingConnectionResponse
import com.memtrip.eosreach.billing.SkuStub

class BillingFlowAccountCreatedErrorOnAccountsTestCase : StubTestCase() {

    override fun test() {
        splashRobot.navigateCreateAccount()
        createAccountRobot
            .verifyEnterAccountNameScreen()
            .typeAccountName()
            .selectCreateAccountButton()
            .verifyAccountCreatedScreen()
            .selectAccountCreatedDoneButton()
            .verifyImportAccountError()
            .selectImportKeySettingsButton()
        settingsRobot
            .verifySettingsScreen()
        commonRobot
            .pressBack()
            .pressBack()
        splashRobot
            .navigateCreateAccount()
        createAccountRobot
            .selectImportKeyRetryButton()
        accountListRobot
            .verifyAccountListScreen()
    }

    override fun billingConnectionResponse(): BillingConnectionResponse {
        return BillingConnectionResponse.Success(
            skuDetails(SkuStub.SUCCESS)
        )
    }

    override fun stubApi(): StubApi = object : StubApi(context()) {

        private val request = ErrorOnFirstStubRequest(200, {
            readJsonFile("stub/happypath/happy_path_get_key_accounts.json")
        })

        override fun getKeyAccounts(): Stub = Stub(
            StubMatcher(
                context.getString(R.string.app_default_eos_endpoint_root),
                Regex("v1/history/get_key_accounts$")
            ),
            request
        )
    }
}