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
package com.memtrip.eosreach.app.account.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountBundle
import com.memtrip.eosreach.app.account.AccountListAdapter
import com.memtrip.eosreach.app.account.DefaultAccountActivity.Companion.accountDefaultIntent
import com.memtrip.eosreach.app.manage.ManageCreateAccountActivity.Companion.manageCreateAccountIntent
import com.memtrip.eosreach.app.manage.ManageImportKeyActivity.Companion.manageImportKeyIntent
import com.memtrip.eosreach.app.settings.SettingsActivity.Companion.settingsIntent
import com.memtrip.eosreach.db.account.AccountEntity
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_navigation_fragment.*
import kotlinx.android.synthetic.main.account_navigation_fragment.view.*
import javax.inject.Inject

class AccountNavigationFragment
    : MviFragment<AccountNavigationIntent, AccountListRenderAction, AccountNavigationViewState, AccountListViewLayout>(), AccountListViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: AccountListViewRenderer

    private lateinit var adapter: AccountListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.account_navigation_fragment, container, false)

        val adapterInteraction: PublishSubject<Interaction<AccountEntity>> = PublishSubject.create()
        adapter = AccountListAdapter(context!!, adapterInteraction)
        view.account_navigation_accounts_recyclerview.adapter = adapter

        view.account_navigation_import_key.setOnClickListener {
            startActivity(manageImportKeyIntent(context!!))
        }

        view.account_navigation_create_account.setOnClickListener {
            startActivity(manageCreateAccountIntent(context!!))
        }

        view.account_navigation_settings.setOnClickListener {
            startActivity(settingsIntent(context!!))
        }

        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<AccountNavigationIntent> = Observable.mergeArray(
        Observable.just(AccountNavigationIntent.Init),
        adapter.interaction.map {
            AccountNavigationIntent.AccountSelected(it.data)
        },
        Observable.merge(
            account_list_error_view.retryClick(),
            RxView.clicks(account_navigation_refresh_accounts)
        ).map {
            AccountNavigationIntent.RefreshAccounts
        }
    )

    override fun layout(): AccountListViewLayout = this

    override fun model(): AccountNavigationViewModel = getViewModel(viewModelFactory)

    override fun render(): AccountListViewRenderer = render

    override fun showProgress() {
        account_navigation_accounts_progressbar.visible()
        account_navigation_accounts_recyclerview.gone()
        account_navigation_accounts_error_container.gone()
        account_navigation_no_accounts_container.gone()
    }

    override fun showError() {
        account_navigation_accounts_progressbar.gone()
        account_navigation_accounts_error_container.visible()
        account_list_error_view.populate(
            getString(R.string.accounts_navigation_error_title),
            getString(R.string.accounts_navigation_error_body))
    }

    override fun populate(accounts: List<AccountEntity>) {
        account_navigation_accounts_progressbar.gone()
        account_navigation_accounts_recyclerview.visible()
        adapter.clear()
        adapter.populate(accounts)
    }

    override fun navigateToAccount(accountBundle: AccountBundle) {
        model().publish(AccountNavigationIntent.Idle)
        val intent = accountDefaultIntent(accountBundle, context!!)
        startActivity(intent)
        activity!!.finish()
    }

    override fun showNoAccounts() {
        account_navigation_accounts_progressbar.gone()
        account_navigation_no_accounts_container.visible()
    }
}
