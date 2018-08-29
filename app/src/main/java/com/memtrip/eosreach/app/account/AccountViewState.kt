package com.memtrip.eosreach.app.account

import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.balance.AccountBalances
import com.memtrip.mxandroid.MxViewState

data class AccountViewState(val view: View) : MxViewState {

    sealed class View {
        object Idle : View()
        object OnProgress : View()
        data class OnSuccess(
            val eosAccount: EosAccount,
            val accountBalances: AccountBalances
        ) : View()
        object OnError : View()
    }
}