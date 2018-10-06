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
package com.memtrip.eosreach.app.settings.viewprivatekeys

import android.app.Application
import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eosreach.db.account.GetAccountNamesForPublicKey
import com.memtrip.eosreach.utils.RxSchedulers
import com.memtrip.eosreach.wallet.EosKeyManager
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class ViewPrivateKeysViewModel @Inject internal constructor(
    private val keyManager: EosKeyManager,
    private val getAccountNamesForPublicKey: GetAccountNamesForPublicKey,
    application: Application
) : MxViewModel<ViewPrivateKeysIntent, ViewPrivateKeysRenderAction, ViewPrivateKeysViewState>(
    ViewPrivateKeysViewState(view = ViewPrivateKeysViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: ViewPrivateKeysIntent): Observable<ViewPrivateKeysRenderAction> = when (intent) {
        is ViewPrivateKeysIntent.Init -> Observable.just(ViewPrivateKeysRenderAction.Idle)
        ViewPrivateKeysIntent.DecryptPrivateKeys -> showPrivateKeys()
    }

    override fun reducer(previousState: ViewPrivateKeysViewState, renderAction: ViewPrivateKeysRenderAction): ViewPrivateKeysViewState = when (renderAction) {
        ViewPrivateKeysRenderAction.Idle -> previousState.copy(
            view = ViewPrivateKeysViewState.View.Idle)
        ViewPrivateKeysRenderAction.OnProgress -> previousState.copy(
            view = ViewPrivateKeysViewState.View.OnProgress)
        is ViewPrivateKeysRenderAction.ShowPrivateKeys -> previousState.copy(
            view = ViewPrivateKeysViewState.View.ShowPrivateKeys(renderAction.viewKeyPair))
        ViewPrivateKeysRenderAction.NoPrivateKeys -> previousState.copy(
            view = ViewPrivateKeysViewState.View.NoPrivateKeys)
    }

    private fun showPrivateKeys(): Observable<ViewPrivateKeysRenderAction> {
        return keyManager.getPrivateKeys().flatMap<ViewPrivateKeysRenderAction> { privateKeys ->
            if (privateKeys.isEmpty()) {
                Single.just(ViewPrivateKeysRenderAction.NoPrivateKeys)
            } else {
                getAccountsForPrivateKey(privateKeys)
            }
        }.onErrorReturn {
            ViewPrivateKeysRenderAction.NoPrivateKeys
        }.toObservable().startWith(ViewPrivateKeysRenderAction.OnProgress)
    }

    private fun getAccountsForPrivateKey(privateKeys: List<EosPrivateKey>): Single<ViewPrivateKeysRenderAction> {
        return Observable
            .fromIterable(privateKeys)
            .concatMap { eosPrivateKey ->
                Observable.zip(
                    Observable.just(eosPrivateKey),
                    getAccountNamesForPublicKey.names(eosPrivateKey.publicKey.toString()).toObservable(),
                    BiFunction<EosPrivateKey, List<String>, ViewKeyPair> { _, names ->
                        ViewKeyPair(eosPrivateKey, names)
                    }
                )
            }
            .toList()
            .map<ViewPrivateKeysRenderAction> {
                ViewPrivateKeysRenderAction.ShowPrivateKeys(it)
            }
            .onErrorReturn {
                ViewPrivateKeysRenderAction.NoPrivateKeys
            }
    }
}