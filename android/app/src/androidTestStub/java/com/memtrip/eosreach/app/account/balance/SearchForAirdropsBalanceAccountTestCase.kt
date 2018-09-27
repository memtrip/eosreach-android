package com.memtrip.eosreach.app.account.balance

import android.os.SystemClock
import com.memtrip.eosreach.R
import com.memtrip.eosreach.StubTestCase
import com.memtrip.eosreach.api.StubApi
import com.memtrip.eosreach.api.stub.Stub
import com.memtrip.eosreach.api.stub.StubMatcher
import com.memtrip.eosreach.api.stub.bodyToString
import com.memtrip.eosreach.api.stub.request.BasicStubRequest
import com.memtrip.eosreach.api.stub.request.ConditionalStubRequest

class SearchForAirdropsBalanceAccountTestCase : StubTestCase() {

    override fun test() {
        splashRobot.navigateImportKey()
        importKeyRobot
            .typePrivateKey("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3")
            .selectImportButton()
        accountListRobot
            .selectFirstAccountRow()
        accountRobot
            .verifyAccountSuccess()
            .verifyAvailableBalance()
        balanceRobot
            .verifyBalanceScreen()
            .verifyTokenTitle()
            .selectAirDropButton()

        SystemClock.sleep(1500) // TODO: why does this require a sleep? This indicates a threading bug.

        balanceRobot
            .verifySysBalanceRow()
            .verifyEdnaBalanceRow()
    }

    override fun stubApi(): StubApi = object : StubApi(context()) {

        override fun getCurrencyBalance(): Stub = Stub(
            StubMatcher(
                context.getString(R.string.app_default_eos_endpoint_root),
                Regex("v1/chain/get_currency_balance$")
            ),
            ConditionalStubRequest { request ->
                return@ConditionalStubRequest if (bodyToString(request) == readJsonFile("stub/request/request_get_currency_balance_edna.json")) {
                    BasicStubRequest(200, {
                        readJsonFile("stub/happypath/happy_path_get_edna_currency_balance.json")
                    })
                } else {
                    BasicStubRequest(200, {
                        readJsonFile("stub/happypath/happy_path_get_sys_currency_balance.json")
                    })
                }
            }
        )
    }
}
