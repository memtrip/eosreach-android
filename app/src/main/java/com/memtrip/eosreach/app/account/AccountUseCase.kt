package com.memtrip.eosreach.app.account

import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.account.EosAccountRequest
import com.memtrip.eosreach.api.balance.AccountBalanceRequest
import com.memtrip.eosreach.api.balance.AccountBalances

import io.reactivex.Single
import javax.inject.Inject

class AccountUseCase @Inject internal constructor(
    private val eosAccountRequest: EosAccountRequest,
    private val accountBalancesRequest: AccountBalanceRequest
) {

    fun getAccountDetails(contractName: String, accountName: String): Single<AccountView> {
        return eosAccountRequest.getAccount(accountName).flatMap { eosAccount ->
            if (eosAccount.success) {
                accountBalancesRequest
                    .getBalance(contractName, eosAccount.data!!.accountName)
                    .map { balances ->
                        if (balances.success) {
                            AccountView.success(eosAccount.data, balances.data)
                        } else {
                            AccountView.error(AccountView.Error.FetchingBalances)
                        }
                    }
            } else {
                Single.just(AccountView.error(AccountView.Error.FetchingAccount))
            }
        }
    }
}

data class AccountView(
    val eosAccount: EosAccount?,
    val balances: AccountBalances?,
    val error: Error?,
    val success: Boolean = error == null
) {

    sealed class Error {
        object FetchingAccount : Error()
        object FetchingBalances : Error()
    }

    companion object {

        fun error(type: Error): AccountView {
            return AccountView(null, null, type)
        }

        fun success(eosAccount: EosAccount?, balances: AccountBalances?): AccountView {
            return AccountView(eosAccount, balances, null)
        }
    }
}