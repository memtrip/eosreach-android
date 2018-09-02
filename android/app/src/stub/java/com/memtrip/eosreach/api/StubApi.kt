package com.memtrip.eosreach.api

import android.content.Context
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.stub.Stub
import com.memtrip.eosreach.api.stub.StubMatcher
import com.memtrip.eosreach.api.stub.request.BasicStubRequest

import java.util.Arrays.asList

abstract class StubApi(
    val context: Context
) {

    open fun getAccount(): Stub = Stub(
        StubMatcher(
            context.getString(R.string.app_default_eos_endpoint_root),
            Regex("v1/chain/get_account$")
        ),
        BasicStubRequest(200, {
            readJsonFile("stub/happypath/happy_path_get_account.json")
        })
    )

    open fun getKeyAccounts(): Stub = Stub(
        StubMatcher(
            context.getString(R.string.app_default_eos_endpoint_root),
            Regex("v1/history/get_key_accounts$")
        ),
        BasicStubRequest(200, {
            readJsonFile("stub/happypath/happy_path_get_key_accounts.json")
        })
    )

    open fun getCurrencyBalance(): Stub = Stub(
        StubMatcher(
            context.getString(R.string.app_default_eos_endpoint_root),
            Regex("v1/chain/get_currency_balance$")
        ),
        BasicStubRequest(200, {
            readJsonFile("stub/happypath/happy_path_get_currency_balance.json")
        })
    )

    open fun getActions(): Stub = Stub(
        StubMatcher(
            context.getString(R.string.app_default_eos_endpoint_root),
            Regex("v1/history/get_actions$")
        ),
        BasicStubRequest(200, {
            readJsonFile("")
        })
    )

    open fun getPriceForCurrency(): Stub = Stub(
        StubMatcher(
            context.getString(R.string.app_default_utility_endpoint_root),
            Regex("price/(.*)$")
        ),
        BasicStubRequest(200, {
            readJsonFile("stub/happypath/happy_path_price.json")
        })
    )

    private fun readJsonFile(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use {
            it.readText()
        }
    }

    fun stubs(): List<Stub> = asList(
        getAccount(),
        getKeyAccounts(),
        getCurrencyBalance(),
        getActions(),
        getPriceForCurrency()
    )
}