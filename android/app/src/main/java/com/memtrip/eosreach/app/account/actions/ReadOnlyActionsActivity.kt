package com.memtrip.eosreach.app.account.actions

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.account.AccountTheme
import com.memtrip.eosreach.uikit.gone
import kotlinx.android.synthetic.main.account_actions_activity.*

class ReadOnlyActionsActivity : ActionsActivity() {

    override fun accountTheme(): AccountTheme = AccountTheme.READ_ONLY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        account_actions_navigation.gone()
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        theme.applyStyle(R.style.ReadOnlyAppTheme, true)
        return theme
    }

    companion object {

        fun actionsReadOnlyIntent(
            contractAccountBalance: ContractAccountBalance,
            context: Context
        ): Intent {
            return with(Intent(context, ReadOnlyActionsActivity::class.java)) {
                putExtra(CONTRACT_ACCOUNT_BALANCE_EXTRA, contractAccountBalance)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                this
            }
        }
    }
}