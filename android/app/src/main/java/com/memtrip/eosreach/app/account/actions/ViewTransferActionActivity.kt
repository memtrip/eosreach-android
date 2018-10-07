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
package com.memtrip.eosreach.app.account.actions

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.actions.model.AccountAction
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountTheme
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.account_actions_view_transfer.*
import javax.inject.Inject

class ViewTransferActionActivity
    : MviActivity<ViewTransferActionIntent, ViewTransferActionRenderAction, ViewTransferActionViewState, ViewTransferActionViewLayout>(), ViewTransferActionViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ViewTransferActionViewRenderer

    private lateinit var accountAction: AccountAction.Transfer
    private lateinit var accountTheme: AccountTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        accountTheme = accountThemeExtra(intent)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_actions_view_transfer)
        setSupportActionBar(account_actions_view_transfer_action_toolbar)
        supportActionBar!!.title = getString(R.string.account_actions_view_transfer_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        accountAction = actionsExtra(intent)
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        if (accountTheme == AccountTheme.READ_ONLY) {
            theme.applyStyle(R.style.ReadOnlyAppTheme, true)
        }
        return theme
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<ViewTransferActionIntent> = Observable.merge(
        Observable.just(ViewTransferActionIntent.Init(accountAction)),
        RxView.clicks(account_actions_view_transaction_block_explorer_button).map {
            ViewTransferActionIntent.ViewTransactionBlockExplorer(accountAction.tranactionId)
        }
    )

    override fun layout(): ViewTransferActionViewLayout = this

    override fun model(): ViewTransferActionViewModel = getViewModel(viewModelFactory)

    override fun render(): ViewTransferActionViewRenderer = render

    override fun populate(accountAction: AccountAction.Transfer) {

        account_actions_view_transfer_details.populate(
            accountAction.quantity,
            accountAction.to,
            accountAction.from,
            accountAction.memo,
            accountAction.contractAccountBalance)

        account_actions_view_transfer_details.populateDate(
            accountAction.formattedDate)
    }

    override fun viewTransactionBlockExplorer(transactionId: String) {
        model().publish(ViewTransferActionIntent.Idle)
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
            getString(R.string.transaction_view_confirm_block_explorer_url, transactionId))))
    }

    companion object {

        private const val ACCOUNT_ACTION_TRANSFER = "ACCOUNT_ACTION_TRANSFER"
        private const val ACCOUNT_THEME = "ACCOUNT_THEME"

        fun viewTransferActionIntent(
            accountAction: AccountAction.Transfer,
            accountTheme: AccountTheme,
            context: Context
        ): Intent {
            return with (Intent(context, ViewTransferActionActivity::class.java)) {
                putExtra(ACCOUNT_ACTION_TRANSFER, accountAction)
                putExtra(ACCOUNT_THEME, accountTheme)
                this
            }
        }

        fun actionsExtra(intent: Intent): AccountAction.Transfer {
            return intent.getParcelableExtra(ACCOUNT_ACTION_TRANSFER) as AccountAction.Transfer
        }

        fun accountThemeExtra(intent: Intent): AccountTheme {
            return intent.getSerializableExtra(ACCOUNT_THEME) as AccountTheme
        }
    }
}