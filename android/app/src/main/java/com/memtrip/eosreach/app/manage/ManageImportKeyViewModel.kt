package com.memtrip.eosreach.app.manage

import android.app.Application
import com.memtrip.eosreach.api.accountforkey.AccountForPublicKeyRequest

import com.memtrip.eosreach.app.issue.importkey.ImportKeyViewModel
import com.memtrip.eosreach.db.account.InsertAccountsForPublicKey
import com.memtrip.eosreach.utils.RxSchedulers
import com.memtrip.eosreach.wallet.EosKeyManager
import javax.inject.Inject

class ManageImportKeyViewModel @Inject internal constructor(
    accountForKeyRequest: AccountForPublicKeyRequest,
    eosKeyManager: EosKeyManager,
    insertAccountsForPublicKey: InsertAccountsForPublicKey,
    rxSchedulers: RxSchedulers,
    application: Application
) : ImportKeyViewModel(
    accountForKeyRequest,
    eosKeyManager,
    insertAccountsForPublicKey,
    rxSchedulers,
    application
)