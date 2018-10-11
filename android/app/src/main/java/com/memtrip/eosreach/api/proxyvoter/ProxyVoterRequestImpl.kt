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
package com.memtrip.eosreach.api.proxyvoter

import com.memtrip.eos.chain.actions.query.proxy.GetRegProxyInfo
import com.memtrip.eosreach.api.Result
import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class ProxyVoterRequestImpl @Inject internal constructor(
    private val getRegProxyInfo: GetRegProxyInfo,
    private val rxSchedulers: RxSchedulers
) : ProxyVoterRequest {

    override fun getProxyVoters(nextAccount: String): Single<Result<List<ProxyVoterDetails>, ProxyVoterError>> {
        return getRegProxyInfo.getProxies(50, nextAccount)
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background()).map { results ->
                Result<List<ProxyVoterDetails>, ProxyVoterError>(results.map { proxyJson ->
                    ProxyVoterDetails(
                        proxyJson.owner,
                        proxyJson.name,
                        proxyJson.website,
                        proxyJson.slogan,
                        proxyJson.philosophy,
                        proxyJson.logo_256)
                })
            }.onErrorReturn {
                Result(ProxyVoterError())
            }
    }

    override fun getProxy(accountName: String): Single<Result<ProxyVoterDetails, ProxyVoterError>> {
        return getRegProxyInfo.getProxy(accountName)
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background()).map { proxyJson ->
                Result<ProxyVoterDetails, ProxyVoterError>(ProxyVoterDetails(
                    proxyJson.owner,
                    proxyJson.name,
                    proxyJson.website,
                    proxyJson.slogan,
                    proxyJson.philosophy,
                    proxyJson.logo_256))
            }.onErrorReturn {
                Result(ProxyVoterError())
            }
    }
}