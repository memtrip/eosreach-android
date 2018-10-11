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
package com.memtrip.eosreach.api.customtokens

import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.model.contract.request.GetTableRows
import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class CustomTokensRequestImpl @Inject internal constructor(
    private val chainApi: ChainApi,
    private val rxSchedulers: RxSchedulers
) : CustomTokensRequest {

    override fun getCustomTokens(): Single<TokenParent> {
        return chainApi.getTableRows(GetTableRows(
            "customtokens",
            "customtokens",
            "tokens",
            "",
            true,
            100,
            "",
            "",
            "",
            "",
            ""
        )).observeOn(rxSchedulers.main()).subscribeOn(rxSchedulers.background()).map { response ->
            if (response.isSuccessful) {
                TokenParent(response.body()!!.rows.map { token ->
                    Token(
                        token.get("uuid") as Double,
                        token.get("owner") as String,
                        token.get("customtoken") as String,
                        token.get("customasset") as String)
                }.filter { token ->
                    token.customtoken != "eosio.token"
                })
            } else {
                throw NoAirdropsFound()
            }
        }.onErrorReturn {
            throw NoAirdropsFound()
        }
    }
}