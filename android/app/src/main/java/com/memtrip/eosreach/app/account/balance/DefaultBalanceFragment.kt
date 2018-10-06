package com.memtrip.eosreach.app.account.balance

import android.os.Bundle
import com.memtrip.eosreach.api.balance.AccountBalanceList
import com.memtrip.eosreach.api.balance.ContractAccountBalance

import com.memtrip.eosreach.app.account.actions.DefaultActionsActivity.Companion.actionsDefaultIntent
import dagger.android.support.AndroidSupportInjection

class DefaultBalanceFragment : BalanceFragment() {

    override fun navigateToActions(
        contractAccountBalance: ContractAccountBalance
    ) {
        model().publish(BalanceIntent.Idle)
        startActivity(actionsDefaultIntent(contractAccountBalance, context!!))
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }
}