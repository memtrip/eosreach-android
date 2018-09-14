package com.memtrip.eosreach.app.account

import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.account.EosAccountRequest
import com.memtrip.eosreach.api.balance.AccountBalanceList
import com.memtrip.eosreach.api.balance.AccountBalanceRequest
import com.memtrip.eosreach.api.eosprice.EosPrice
import com.memtrip.eosreach.app.price.EosPriceUseCase
import io.reactivex.Single
import javax.inject.Inject

class AccountUseCase @Inject internal constructor(
    private val eosAccountRequest: EosAccountRequest,
    private val accountBalancesRequest: AccountBalanceRequest,
    private val eosPriceUseCase: EosPriceUseCase
) {

    fun getAccountDetails(contractName: String, accountName: String): Single<AccountView> {
        return eosPriceUseCase.getPrice()
            .onErrorReturn {
                EosPrice.unavailable()
            }
            .flatMap { price ->
                getAccount(contractName, accountName, price)
            }
    }

    private fun getAccount(
        contractName: String,
        accountName: String,
        eosPrice: EosPrice
    ): Single<AccountView> {
        return eosAccountRequest.getAccount(accountName).flatMap { response ->
            if (response.success) {
                val eosAccount = response.data!!
                accountBalancesRequest
                    .getBalance(contractName, eosAccount.accountName, eosAccount.balance.symbol, eosPrice)
                    .map { balances ->
                        if (balances.success) {
                            AccountView.success(eosPrice, eosAccount, balances.data)
                        } else {
                            AccountView.error(AccountView.Error.FetchingBalances)
                        }
                    }
            } else {
                Single.just(AccountView.error(AccountView.Error.FetchingAccount))
            }
        }.onErrorReturn {
            AccountView.error(AccountView.Error.FetchingAccount)
        }
    }
}

data class AccountView(
    val eosPrice: EosPrice?,
    val eosAccount: EosAccount?,
    val balances: AccountBalanceList?,
    val error: Error? = null,
    val success: Boolean = error == null
) {

    sealed class Error {
        object FetchingAccount : Error()
        object FetchingBalances : Error()
    }

    companion object {

        fun error(type: Error): AccountView {
            return AccountView(null, null, null, type)
        }

        fun success(eosPrice: EosPrice?, eosAccount: EosAccount?, balances: AccountBalanceList?): AccountView {
            return AccountView(eosPrice, eosAccount, balances)
        }
    }
}