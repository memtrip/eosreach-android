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
package com.memtrip.eosreach.app.issue.importkey

import android.app.Application
import com.memtrip.eosreach.R

import com.memtrip.eosreach.api.accountforkey.AccountForPublicKeyRequest

import com.memtrip.eosreach.db.account.InsertAccountsForPublicKey
import com.memtrip.eosreach.utils.RxSchedulers
import com.memtrip.eosreach.wallet.EosKeyManager

import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single

abstract class ImportKeyViewModel(
    private val accountForKeyRequest: AccountForPublicKeyRequest,
    private val eosKeyManager: EosKeyManager,
    private val insertAccountsForPublicKey: InsertAccountsForPublicKey,
    private val rxSchedulers: RxSchedulers,
    application: Application
) : MxViewModel<ImportKeyIntent, ImportKeyRenderAction, ImportKeyViewState>(
    ImportKeyViewState(view = ImportKeyViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: ImportKeyIntent): Observable<ImportKeyRenderAction> = when (intent) {
        is ImportKeyIntent.Init -> Observable.just(ImportKeyRenderAction.Idle)
        is ImportKeyIntent.ImportKey -> importKey(intent.privateKey)
        ImportKeyIntent.ViewSource -> Observable.just(ImportKeyRenderAction.NavigateToGithubSource)
        ImportKeyIntent.NavigateToSettings -> Observable.just(ImportKeyRenderAction.NavigateToSettings)
    }

    override fun reducer(previousState: ImportKeyViewState, renderAction: ImportKeyRenderAction): ImportKeyViewState = when (renderAction) {
        ImportKeyRenderAction.Idle -> previousState.copy(view = ImportKeyViewState.View.Idle)
        ImportKeyRenderAction.OnProgress -> previousState.copy(view = ImportKeyViewState.View.OnProgress)
        ImportKeyRenderAction.OnSuccess -> previousState.copy(view = ImportKeyViewState.View.OnSuccess)
        is ImportKeyRenderAction.OnError -> previousState.copy(view = ImportKeyViewState.View.OnError(renderAction.error))
        ImportKeyRenderAction.NavigateToGithubSource -> previousState.copy(view = ImportKeyViewState.View.NavigateToGithubSource)
        ImportKeyRenderAction.NavigateToSettings -> previousState.copy(view = ImportKeyViewState.View.NavigateToSettings)
    }

    private fun importKey(privateKey: String): Observable<ImportKeyRenderAction> {
        return eosKeyManager.createEosPrivateKey(privateKey).flatMap { eosPrivateKey ->
            eosKeyManager.importPrivateKey(eosPrivateKey)
                .observeOn(rxSchedulers.main())
                .subscribeOn(rxSchedulers.background())
                .flatMap {
                    accountForKeyRequest.getAccountsForKey(it)
                }.flatMap { result ->
                    if (result.success) {
                        if (result.data!!.accounts.isEmpty()) {
                            Single.just(ImportKeyRenderAction.OnError(
                                context().getString(R.string.issue_import_key_error_no_accounts)))
                        } else {
                            insertAccountsForPublicKey.replace(
                                result.data.publicKey,
                                result.data.accounts
                            ).map {
                                ImportKeyRenderAction.OnSuccess
                            }
                        }
                    } else {
                        Single.just(ImportKeyRenderAction.OnError(
                            context().getString(R.string.issue_import_key_error_generic)))
                    }
                }.onErrorReturn {
                    ImportKeyRenderAction.OnError(
                        context().getString(R.string.issue_import_key_error_generic))
                }
        }.onErrorReturn {
            ImportKeyRenderAction.OnError(
                context().getString(R.string.issue_import_key_error_invalid_private_key_format))
        }.toObservable()
    }
}