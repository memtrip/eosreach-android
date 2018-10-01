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
package com.memtrip.eosreach.app.welcome

import android.app.Application
import com.memtrip.eosreach.db.account.CountAccounts
import com.memtrip.eosreach.db.account.GetAccountByName
import com.memtrip.eosreach.db.sharedpreferences.AccountListSelection
import com.memtrip.eosreach.wallet.EosKeyManager
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class EntryViewModel @Inject internal constructor(
    private val eosKeyManager: EosKeyManager,
    private val countAccounts: CountAccounts,
    private val getAccountByName: GetAccountByName,
    private val selectedAccount: AccountListSelection,
    application: Application
) : MxViewModel<EntryIntent, AccountListRenderAction, EntryViewState>(
    EntryViewState(view = EntryViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: EntryIntent): Observable<AccountListRenderAction> = when (intent) {
        is EntryIntent.Init -> hasAccounts()
    }

    override fun reducer(previousState: EntryViewState, renderAction: AccountListRenderAction): EntryViewState = when (renderAction) {
        AccountListRenderAction.OnProgress -> previousState.copy(view = EntryViewState.View.Idle)
        AccountListRenderAction.NavigateToSplash -> previousState.copy(view = EntryViewState.View.NavigateToSplash)
        AccountListRenderAction.NavigateToAccountList -> previousState.copy(view = EntryViewState.View.NavigateToAccountList)
        is AccountListRenderAction.NavigateToAccount -> previousState.copy(view = EntryViewState.View.NavigateToAccount(renderAction.accountEntity))
        AccountListRenderAction.OnError -> previousState.copy(view = EntryViewState.View.OnError)
        AccountListRenderAction.OnRsaEncryptionFailed -> previousState.copy(view = EntryViewState.View.OnRsaEncryptionFailed)
    }

    private fun hasAccounts(): Observable<AccountListRenderAction> {
        return eosKeyManager.verifyDeviceSupportsRsaEncryption().flatMap<AccountListRenderAction> { rsaVerified ->
            if (rsaVerified) {
                countAccounts.count()
                    .flatMap<AccountListRenderAction> { count ->
                        if (count > 0) {
                            if (selectedAccount.exists()) {
                                getAccountByName.select(selectedAccount.get())
                                    .map { accountEntity ->
                                        AccountListRenderAction.NavigateToAccount(accountEntity)
                                    }
                            } else {
                                Single.just(AccountListRenderAction.NavigateToAccountList)
                            }
                        } else {
                            Single.just(AccountListRenderAction.NavigateToSplash)
                        }
                    }
            } else {
                Single.just(AccountListRenderAction.OnRsaEncryptionFailed)
            }
        }.onErrorReturn {
            AccountListRenderAction.OnRsaEncryptionFailed
        }.onErrorReturn {
            AccountListRenderAction.OnError
        }.toObservable().startWith(AccountListRenderAction.OnProgress)
    }
}