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
package com.memtrip.eosreach.api.account

import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.model.account.request.AccountName
import com.memtrip.eos.http.rpc.model.account.response.SelfDelegatedBandwidth
import com.memtrip.eos.http.rpc.model.account.response.TotalResources
import com.memtrip.eos.http.rpc.model.account.response.VoterInfo

import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.app.price.BalanceFormatter
import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class EosAccountRequestImpl @Inject internal constructor(
    private val chainApi: ChainApi,
    private val rxSchedulers: RxSchedulers
) : EosAccountRequest {

    override fun getAccount(accountName: String): Single<Result<EosAccount, AccountError>> {

        return chainApi.getAccount(AccountName(accountName))
            .subscribeOn(rxSchedulers.background())
            .observeOn(rxSchedulers.main())
            .map {
                if (it.isSuccessful) {
                    val account = it.body()!!
                    val stakedNetBalance = stakedNetBalance(account.self_delegated_bandwidth)
                    val stakedCpuBalance = stakedCpuBalance(account.self_delegated_bandwidth)
                    Result(
                        EosAccount(
                            account.account_name,
                            inferBalanceSymbol(
                                account.core_liquid_balance,
                                stakedNetBalance,
                                stakedCpuBalance,
                                account.total_resources),
                            EosAccountResource(
                                account.net_limit.used,
                                account.net_limit.available,
                                stakedNetBalance,
                                delegatedNetBalance(account.total_resources, stakedNetBalance)),
                            EosAccountResource(
                                account.cpu_limit.used,
                                account.cpu_limit.available,
                                stakedCpuBalance,
                                delegatedCpuBalance(account.total_resources, stakedCpuBalance)),
                            EosAccountResource(
                                account.ram_usage,
                                account.ram_quota),
                            eosCurrentVote(account.voter_info)
                        )
                    )
                } else {
                    Result<EosAccount, AccountError>(
                        AccountError.FailedRetrievingAccount(it.code(), it.errorBody()))
                }
        }.onErrorReturn { Result(AccountError.GenericError) }
    }

    private fun inferBalanceSymbol(
        balance: String?,
        netBalance: Balance?,
        cpuBalance: Balance?,
        totalResources: TotalResources?
    ): Balance {
        return if (balance != null) {
            BalanceFormatter.deserialize(balance)
        } else if (netBalance != null) {
            BalanceFormatter.create(0.0, netBalance.symbol)
        } else if (cpuBalance != null) {
            BalanceFormatter.create(0.0, cpuBalance.symbol)
        } else if (totalResources != null) {
            BalanceFormatter.deserialize(totalResources.cpu_weight)
        } else {
            throw IllegalStateException("Could not infer account balance symbol")
        }
    }

    private fun stakedNetBalance(selfDelegatedBandwidth: SelfDelegatedBandwidth?): Balance? {
        return if (selfDelegatedBandwidth != null) {
            return BalanceFormatter.deserialize(selfDelegatedBandwidth.net_weight)
        } else {
            null
        }
    }

    private fun delegatedNetBalance(totalResources: TotalResources?, stakedNetBalance: Balance?): Balance? {
        val stakedNetBalanceAmount: Double = stakedNetBalance?.amount ?: 0.0
        return if (totalResources != null) {
            val totalNetBalance = BalanceFormatter.deserialize(totalResources.net_weight)
            val delegateBalance: Double = totalNetBalance.amount - stakedNetBalanceAmount
            BalanceFormatter.create(delegateBalance, totalNetBalance.symbol)
        } else {
            null
        }
    }

    private fun stakedCpuBalance(selfDelegatedBandwidth: SelfDelegatedBandwidth?): Balance? {
        return if (selfDelegatedBandwidth != null) {
            return BalanceFormatter.deserialize(selfDelegatedBandwidth.cpu_weight)
        } else {
            null
        }
    }

    private fun delegatedCpuBalance(totalResources: TotalResources?, stakedCpuBalance: Balance?): Balance? {
        val stakedCpuBalanceAmount: Double = stakedCpuBalance?.amount ?: 0.0
        return if (totalResources != null) {
            val totalCpuBalance = BalanceFormatter.deserialize(totalResources.cpu_weight)
            val delegateBalance: Double = totalCpuBalance.amount - stakedCpuBalanceAmount
            BalanceFormatter.create(delegateBalance, totalCpuBalance.symbol)
        } else {
            null
        }
    }

    private fun eosCurrentVote(voterInfo: VoterInfo?): EosAccountVote? {
        if (voterInfo != null) {
            return EosAccountVote(
                voterInfo.proxy,
                voterInfo.producers,
                voterInfo.staked,
                voterInfo.last_vote_weight,
                voterInfo.proxied_vote_weight,
                voterInfo.is_proxy == 1,
                voterInfo.proxy.isNotEmpty()
            )
        } else {
            return null
        }
    }
}