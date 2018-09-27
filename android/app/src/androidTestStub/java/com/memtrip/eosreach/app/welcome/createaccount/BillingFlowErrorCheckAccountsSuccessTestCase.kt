package com.memtrip.eosreach.app.welcome.createaccount

import com.memtrip.eosreach.StubTestCase
import com.memtrip.eosreach.billing.BillingConnectionResponse
import com.memtrip.eosreach.billing.SkuStub

/**
 * When the billing flow succeeds, but the createAccount endpoint fails, we determine
 * whether the account was created by calling get_key_accounts with the current
 * session public key.
 *
 * In this test case an account is retrieved, the user is displayed the Account created screen,
 * and all is fine.
 */
class BillingFlowErrorCheckAccountsSuccessTestCase : StubTestCase() {

    override fun test() {
        splashRobot
            .navigateCreateAccount()
        createAccountRobot
            .verifyEnterAccountNameScreen()
            .typeAccountName()
            .selectCreateAccountButton()
            .verifyAccountCreatedScreen()
        commonRobot
            .pressHomeUp()
        accountListRobot
            .verifyAccountListScreen()
    }

    override fun billingConnectionResponse(): BillingConnectionResponse {
        return BillingConnectionResponse.Success(
            skuDetails(SkuStub.SUCCESS)
        )
    }
}