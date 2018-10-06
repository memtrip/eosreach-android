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
import com.memtrip.eosreach.db.account.GetAccounts
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
    private val getAccounts: GetAccounts,
    application: Application
) : MxViewModel<EntryIntent, EntryRenderAction, EntryViewState>(
    EntryViewState(view = EntryViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: EntryIntent): Observable<EntryRenderAction> = when (intent) {
        is EntryIntent.Init -> hasAccounts()
    }

    override fun reducer(previousState: EntryViewState, renderAction: EntryRenderAction): EntryViewState = when (renderAction) {
        EntryRenderAction.OnProgress -> previousState.copy(view = EntryViewState.View.Idle)
        EntryRenderAction.NavigateToSplash -> previousState.copy(view = EntryViewState.View.NavigateToSplash)
        is EntryRenderAction.NavigateToAccount -> previousState.copy(view = EntryViewState.View.NavigateToAccount(renderAction.accountEntity))
        EntryRenderAction.OnError -> previousState.copy(view = EntryViewState.View.OnError)
        EntryRenderAction.OnRsaEncryptionFailed -> previousState.copy(view = EntryViewState.View.OnRsaEncryptionFailed)
    }

    private fun hasAccounts(): Observable<EntryRenderAction> {
        return eosKeyManager.verifyDeviceSupportsRsaEncryption().flatMap<EntryRenderAction> { rsaVerified ->
            if (rsaVerified) {
                countAccounts.count()
                    .flatMap<EntryRenderAction> { count ->
                        if (count > 0) {
                            if (selectedAccount.exists()) {
                                getAccountByName.select(selectedAccount.get())
                                    .map { accountEntity ->
                                        EntryRenderAction.NavigateToAccount(accountEntity)
                                    }
                            } else {
                                getAccounts.select().map { accountEntity ->
                                    EntryRenderAction.NavigateToAccount(accountEntity[0])
                                }
                            }
                        } else {
                            Single.just(EntryRenderAction.NavigateToSplash)
                        }
                    }
            } else {
                Single.just(EntryRenderAction.OnRsaEncryptionFailed)
            }
        }.onErrorReturn {
            EntryRenderAction.OnRsaEncryptionFailed
        }.onErrorReturn {
            EntryRenderAction.OnError
        }.toObservable().startWith(EntryRenderAction.OnProgress)
    }
}