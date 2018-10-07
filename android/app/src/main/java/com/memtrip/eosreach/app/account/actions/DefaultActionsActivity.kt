package com.memtrip.eosreach.app.account.actions

import android.content.Context
import android.content.Intent
import com.memtrip.eosreach.api.balance.ContractAccountBalance

class DefaultActionsActivity : ActionsActivity() {

    companion object {

        fun actionsDefaultIntent(
            contractAccountBalance: ContractAccountBalance,
            context: Context
        ): Intent {
            return with (Intent(context, DefaultActionsActivity::class.java)) {
                putExtra(CONTRACT_ACCOUNT_BALANCE_EXTRA, contractAccountBalance)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                this
            }
        }
    }
}