package com.memtrip.eosreach.app.welcome.createaccount

import android.app.Application
import com.memtrip.eosreach.api.accountforkey.AccountForPublicKeyRequest
import com.memtrip.eosreach.api.eoscreateaccount.EosCreateAccountRequest
import com.memtrip.eosreach.app.issue.createaccount.CreateAccountViewModel
import com.memtrip.eosreach.db.SelectedAccount
import com.memtrip.eosreach.db.account.InsertAccountsForPublicKey
import com.memtrip.eosreach.wallet.EosKeyManager
import javax.inject.Inject

class WelcomeCreateAccountViewModel @Inject internal constructor(
    keyManager: EosKeyManager,
    eosCreateAccountRequest: EosCreateAccountRequest,
    accountForPublicKeyRequest: AccountForPublicKeyRequest,
    insertAccountsForPublicKey: InsertAccountsForPublicKey,
    selectedAccount: SelectedAccount,
    application: Application
) : CreateAccountViewModel(
    keyManager,
    eosCreateAccountRequest,
    accountForPublicKeyRequest,
    insertAccountsForPublicKey,
    selectedAccount,
    application
)