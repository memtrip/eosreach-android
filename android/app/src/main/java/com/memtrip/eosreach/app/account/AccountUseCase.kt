package com.memtrip.eosreach.app.account

import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.model.contract.request.GetCurrencyBalance

import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.account.EosAccountRequest

import com.memtrip.eosreach.api.accountforkey.AccountsForPublicKeyRequestImpl
import com.memtrip.eosreach.api.balance.AccountBalanceList
import com.memtrip.eosreach.api.balance.Balance

import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.api.eosprice.EosPrice
import com.memtrip.eosreach.app.price.BalanceFormatter
import com.memtrip.eosreach.app.price.EosPriceUseCase
import com.memtrip.eosreach.db.airdrop.BalanceEntity
import com.memtrip.eosreach.db.airdrop.GetBalances
import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import retrofit2.Response
import java.util.Arrays.asList
import javax.inject.Inject

class AccountUseCase @Inject internal constructor(
    private val eosAccountRequest: EosAccountRequest,
    private val getBalances: GetBalances,
    private val chainApi: ChainApi,
    private val eosPriceUseCase: EosPriceUseCase,
    private val rxSchedulers: RxSchedulers
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
                getBalances(eosAccount, eosPrice, BalanceEntity(
                    accountName,
                    contractName,
                    eosAccount.balance.symbol
                ))
            } else {
                Single.just(AccountView.error(AccountView.Error.FetchingAccount))
            }
        }.onErrorReturn {
            AccountView.error(AccountView.Error.FetchingAccount)
        }
    }

    private fun getBalances(eosAccount: EosAccount, primaryEosPrice: EosPrice, primaryBalance: BalanceEntity): Single<AccountView> {
        return getBalances.select(eosAccount.accountName).flatMap { balanceEntities ->
            Observable.fromIterable(with(ArrayList(asList(primaryBalance))) {
                addAll(balanceEntities)
                this
            })
                .flatMap { balanceEntity ->
                    Observable.zip(
                        Observable.just(balanceEntity),
                        chainApi.getCurrencyBalance(GetCurrencyBalance(
                            balanceEntity.contractName,
                            balanceEntity.accountName
                        )).toObservable().observeOn(rxSchedulers.main()).subscribeOn(rxSchedulers.background()),
                        BiFunction<BalanceEntity, Response<List<String>>, ContractAccountBalance> { _, response ->
                            if (response.isSuccessful) {
                                val balanceList = response.body()!!
                                if (balanceList.isNotEmpty()) {
                                    val balance = BalanceFormatter.deserialize(balanceList[0])
                                    ContractAccountBalance(
                                        balanceEntity.contractName,
                                        balanceEntity.accountName,
                                        balance,
                                        parsePrice(eosAccount, primaryEosPrice, balance))
                                } else {
                                    ContractAccountBalance.unavailable()
                                }
                            } else {
                                throw AccountsForPublicKeyRequestImpl.InnerAccountFailed()
                            }
                        })
                }.toList()
                .map { contractAccountBalances ->
                    AccountView.success(
                        EosPrice.unavailable(),
                        eosAccount,
                        AccountBalanceList(contractAccountBalances)
                    )
                }
        }
    }

    private fun parsePrice(eosAccount: EosAccount, eosPrice: EosPrice, balance: Balance): EosPrice {
        return if (eosAccount.balance.symbol == balance.symbol) {
            eosPrice
        } else {
            EosPrice.unavailable()
        }
    }
}