package com.memtrip.eosreach.app.account.balance

import com.memtrip.eosreach.api.balance.ContractAccountBalance

import com.memtrip.eosreach.app.account.actions.ReadOnlyActionsActivity.Companion.actionsReadOnlyIntent
import dagger.android.support.AndroidSupportInjection

class ReadOnlyBalanceFragment : BalanceFragment() {

    override fun navigateToActions(
        contractAccountBalance: ContractAccountBalance
    ) {
        model().publish(BalanceIntent.Idle)
        startActivity(actionsReadOnlyIntent(contractAccountBalance, context!!))
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }
}