package com.memtrip.eosreach.api.customtokens

import com.memtrip.eos.http.rpc.ChainApi
import com.memtrip.eos.http.rpc.model.contract.request.GetTableRows
import com.memtrip.eosreach.utils.RxSchedulers
import io.reactivex.Single
import java.io.IOException
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
            true,
            100,
            "",
            "",
            "",
            "",
            ""
        )).map { response ->
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
            it.printStackTrace()
            throw NoAirdropsFound()
        }.observeOn(rxSchedulers.main()).subscribeOn(rxSchedulers.background())
    }
}