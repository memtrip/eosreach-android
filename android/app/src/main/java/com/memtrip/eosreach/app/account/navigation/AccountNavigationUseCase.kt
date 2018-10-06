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
package com.memtrip.eosreach.app.account.navigation

import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.accountforkey.AccountForPublicKeyRequest
import com.memtrip.eosreach.api.accountforkey.AccountsForPublicKey
import com.memtrip.eosreach.db.account.InsertAccountsForPublicKey
import com.memtrip.eosreach.utils.RxSchedulers
import com.memtrip.eosreach.wallet.EosKeyManager
import io.reactivex.Single
import javax.inject.Inject

class AccountNavigationUseCase @Inject internal constructor(
    private val accountForPublicKeyRequest: AccountForPublicKeyRequest,
    private val insertAccountsForPublicKey: InsertAccountsForPublicKey,
    private val eosKeyManager: EosKeyManager,
    private val rxSchedulers: RxSchedulers
) {

    fun refreshAccounts(): Single<Result<List<AccountsForPublicKey>, AccountsListError>> {
        return Single.fromCallable { getAccounts() }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }

    private fun getAccounts(): Result<List<AccountsForPublicKey>, AccountsListError> {
        val publicKeys = eosKeyManager.getAllPublicKeys()
        val results = publicKeys.map { publicKey ->
            accountForPublicKeyRequest.getAccountsForKey(publicKey).flatMap { result ->
                if (result.success) {
                    Single.just(Result(result.data!!))
                } else {
                    Single.just(Result<AccountsForPublicKey, AccountsListError>(AccountsListError.RefreshAccountsFailed))
                }
            }.blockingGet()
        }.filter {
            it.success
        }

        val allAccounts = results.flatMap {
            it.data!!.accounts
        }

        return when {
            results.size != publicKeys.size -> {
                Result(AccountsListError.RefreshAccountsFailed)
            }
            allAccounts.isEmpty() -> {
                Result(AccountsListError.NoAccounts)
            }
            else -> {
                Result(results.map { result ->
                    val accountsForPublicKey = result.data!!
                    insertAccountsForPublicKey.replace(
                        accountsForPublicKey.publicKey,
                        accountsForPublicKey.accounts
                    ).blockingGet()
                    accountsForPublicKey
                })
            }
        }
    }

    sealed class AccountsListError : ApiError {
        object RefreshAccountsFailed : AccountsListError()
        object NoAccounts : AccountsListError()
    }
}