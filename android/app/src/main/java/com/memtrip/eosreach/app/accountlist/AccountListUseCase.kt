package com.memtrip.eosreach.app.accountlist

import com.memtrip.eosreach.api.ApiError
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.accountforkey.AccountForPublicKeyRequest
import com.memtrip.eosreach.api.accountforkey.AccountsForPublicKey
import com.memtrip.eosreach.db.account.InsertAccountsForPublicKey
import com.memtrip.eosreach.utils.RxSchedulers
import com.memtrip.eosreach.wallet.EosKeyManager
import io.reactivex.Single
import javax.inject.Inject

class AccountListUseCase @Inject internal constructor(
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