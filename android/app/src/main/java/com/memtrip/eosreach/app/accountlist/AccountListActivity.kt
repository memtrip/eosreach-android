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
package com.memtrip.eosreach.app.accountlist

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.os.Bundle
import android.view.Menu
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountActivity.Companion.accountIntent
import com.memtrip.eosreach.app.account.AccountBundle
import com.memtrip.eosreach.app.settings.SettingsActivity.Companion.settingsIntent

import com.memtrip.eosreach.db.account.AccountEntity

import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.accounts_list_activity.*
import javax.inject.Inject

class AccountListActivity
    : MviActivity<AccountListIntent, AccountListRenderAction, AccountListViewState, AccountListViewLayout>(), AccountListViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: AccountListViewRenderer

    private lateinit var adapter: AccountListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accounts_list_activity)

        setSupportActionBar(account_list_toolbar)

        val adapterInteraction: PublishSubject<Interaction<AccountEntity>> = PublishSubject.create()
        adapter = AccountListAdapter(this, adapterInteraction)
        account_list_recyclerview.adapter = adapter
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<AccountListIntent> = Observable.mergeArray(
        Observable.just(AccountListIntent.Init),
        adapter.interaction.map {
            AccountListIntent.AccountSelected(it.data)
        },
        RxView.clicks(account_list_no_accounts_settings_button).map {
            AccountListIntent.NavigateToSettings
        },
        RxView.clicks(account_list_error_settings_button).map {
            AccountListIntent.NavigateToSettings
        },
        account_list_error_view.retryClick().map {
            AccountListIntent.RefreshAccounts
        }
    )

    override fun layout(): AccountListViewLayout = this

    override fun model(): AccountListViewModel = getViewModel(viewModelFactory)

    override fun render(): AccountListViewRenderer = render

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.accounts_list_menu, menu)

        menu.findItem(R.id.accounts_list_menu_refresh_accounts).setOnMenuItemClickListener {
            model().publish(AccountListIntent.RefreshAccounts)
            true
        }

        return true
    }

    override fun showProgress() {
        account_list_progressbar.visible()
        account_list_recyclerview.gone()
        account_list_toolbar.gone()
        account_list_error_container.gone()
    }

    override fun showError() {
        account_list_progressbar.gone()
        account_list_error_container.visible()
        account_list_recyclerview.gone()
        account_list_error_view.populate(
            getString(R.string.accounts_list_error_title),
            getString(R.string.accounts_list_error_body))
    }

    override fun populate(accounts: List<AccountEntity>) {
        account_list_progressbar.gone()
        account_list_recyclerview.visible()
        account_list_toolbar.visible()
        adapter.clear()
        adapter.populate(accounts)
    }

    override fun navigateToAccount(accountBundle: AccountBundle) {
        model().publish(AccountListIntent.Idle)
        val intent = accountIntent(accountBundle, this)
        startActivity(intent)
        finish()
    }

    override fun showNoAccounts() {
        account_list_no_accounts_container.visible()
        account_list_progressbar.gone()
    }

    override fun navigateToSettings() {
        model().publish(AccountListIntent.Idle)
        startActivity(settingsIntent(this))
    }

    companion object {

        fun accountListIntent(context: Context): Intent {
            return with (Intent(context, AccountListActivity::class.java)) {
                addFlags(FLAG_ACTIVITY_SINGLE_TOP or FLAG_ACTIVITY_CLEAR_TOP)
                this
            }
        }
    }
}
