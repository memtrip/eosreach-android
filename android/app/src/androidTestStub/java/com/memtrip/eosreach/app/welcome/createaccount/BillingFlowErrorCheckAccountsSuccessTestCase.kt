/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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
        accountRobot
            .verifyAccountScreen()
            .verifyAvailableBalance()
    }

    override fun billingConnectionResponse(): BillingConnectionResponse {
        return BillingConnectionResponse.Success(
            skuDetails(SkuStub.SUCCESS)
        )
    }
}