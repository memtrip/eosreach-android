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
package com.memtrip.eosreach.app.manage

import android.app.Application
import com.memtrip.eosreach.api.accountforkey.AccountForPublicKeyRequest
import com.memtrip.eosreach.api.eoscreateaccount.EosCreateAccountRequest
import com.memtrip.eosreach.app.issue.createaccount.CreateAccountViewModel
import com.memtrip.eosreach.db.account.InsertAccountsForPublicKey
import com.memtrip.eosreach.db.sharedpreferences.AccountListSelection
import com.memtrip.eosreach.db.sharedpreferences.UnusedBillingPurchaseToken
import com.memtrip.eosreach.db.sharedpreferences.UnusedPublicKey
import com.memtrip.eosreach.db.sharedpreferences.UnusedPublicKeyInLimbo
import com.memtrip.eosreach.db.sharedpreferences.UnusedPublicKeyNoAccountsSynced
import com.memtrip.eosreach.wallet.EosKeyManager
import javax.inject.Inject

class ManageCreateAccountViewModel @Inject internal constructor(
    keyManager: EosKeyManager,
    eosCreateAccountRequest: EosCreateAccountRequest,
    accountForPublicKeyRequest: AccountForPublicKeyRequest,
    insertAccountsForPublicKey: InsertAccountsForPublicKey,
    selectedAccount: AccountListSelection,
    unusedPublicKeyInLimbo: UnusedPublicKeyInLimbo,
    unusedBillingPurchaseId: UnusedBillingPurchaseToken,
    unusedPublicKey: UnusedPublicKey,
    unusedPublicKeyNoAccounts: UnusedPublicKeyNoAccountsSynced,
    application: Application
) : CreateAccountViewModel(
    keyManager,
    eosCreateAccountRequest,
    accountForPublicKeyRequest,
    insertAccountsForPublicKey,
    selectedAccount,
    unusedPublicKeyInLimbo,
    unusedBillingPurchaseId,
    unusedPublicKey,
    unusedPublicKeyNoAccounts,
    application
)