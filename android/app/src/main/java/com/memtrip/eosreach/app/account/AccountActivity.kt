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
package com.memtrip.eosreach.app.account

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.accountlist.AccountListActivity.Companion.accountListIntent
import com.memtrip.eosreach.app.manage.ManageCreateAccountActivity.Companion.manageCreateAccountIntent
import com.memtrip.eosreach.app.manage.ManageImportKeyActivity.Companion.manageImportKeyIntent
import com.memtrip.eosreach.app.settings.SettingsActivity.Companion.settingsIntent
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.start
import com.memtrip.eosreach.uikit.stop
import com.memtrip.eosreach.uikit.visible
import com.memtrip.eosreach.utils.ViewPagerOnPageChangeListener
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.account_activity.*
import javax.inject.Inject

class AccountActivity
    : MviActivity<AccountIntent, AccountRenderAction, AccountViewState, AccountViewLayout>(), AccountViewLayout, AccountParentRefresh {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: AccountViewRenderer

    private lateinit var accountBundle: AccountBundle
    private lateinit var page: AccountFragmentPagerAdapter.Page

    private var loaded = false // after data has loaded a dialog is displayed for errors

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_activity)
        account_toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.account_overflow_menu)
        account_toolbar.title = ""
        setSupportActionBar(account_toolbar)
        accountBundle = accountExtra(intent)
        page = pageExtra(intent)
        account_viewpager.currentItem = page.ordinal
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.account_menu, menu)

        menu.findItem(R.id.account_menu_import_key).setOnMenuItemClickListener {
            model().publish(AccountIntent.NavigateToImportKey)
            true
        }

        menu.findItem(R.id.account_menu_create_account).setOnMenuItemClickListener {
            model().publish(AccountIntent.NavigateToCreateAccount)
            true
        }

        menu.findItem(R.id.account_menu_settings).setOnMenuItemClickListener {
            model().publish(AccountIntent.NavigateToSettings)
            true
        }

        return true
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<AccountIntent> = Observable.mergeArray(
        Observable.just(AccountIntent.Init(accountBundle, page)),
        account_error_view.retryClick().map { AccountIntent.Retry(accountBundle) },
        RxView.clicks(account_toolbar_account_name).map { AccountIntent.NavigateToAccountList }
    )

    override fun layout(): AccountViewLayout = this

    override fun model(): AccountViewModel = getViewModel(viewModelFactory)

    override fun render(): AccountViewRenderer = render

    override fun showProgress() {
        account_swipelayout.start()
        account_error_view.gone()
    }

    override fun populateTitle(accountName: String) {
        account_toolbar_account_name.text = accountName
    }

    override fun populate(accountView: AccountView, page: AccountFragmentPagerAdapter.Page) {

        loaded = true

        publishIdleTab(page)

        account_toolbar_account_name.text = accountView.eosAccount!!.accountName
        account_swipelayout.stop()
        account_header_group.visible()

        val accountPagerFragment = AccountFragmentPagerAdapter(
            supportFragmentManager,
            this,
            accountView)
        account_viewpager.adapter = accountPagerFragment
        account_viewpager.offscreenPageLimit = 3
        account_viewpager.currentItem = page.ordinal
        account_viewpager.visible()
        account_viewpager.addOnPageChangeListener(object : ViewPagerOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    AccountFragmentPagerAdapter.Page.BALANCE.ordinal -> {
                        model().publish(AccountIntent.BalanceTabIdle)
                    }
                    AccountFragmentPagerAdapter.Page.RESOURCES.ordinal -> {
                        model().publish(AccountIntent.ResourceTabIdle)
                    }
                    AccountFragmentPagerAdapter.Page.VOTE.ordinal -> {
                        model().publish(AccountIntent.VoteTabIdle)
                    }
                    else -> {
                        model().publish(AccountIntent.BalanceTabIdle)
                    }
                }
            }
        })

        account_tablayout.setupWithViewPager(account_viewpager)

        account_swipelayout.setOnRefreshListener {
            model().publish(AccountIntent.Refresh(accountBundle))
        }
    }

    override fun showPrice(price: String) {
        account_available_balance_value.text = price
    }

    override fun showPriceUnavailable() {
        account_available_balance_value.text = getString(R.string.account_available_balance_unavailable_value)
        account_available_balance_label.text = getString(R.string.account_available_balance_unavailable_label)
    }

    override fun showGetAccountError() {
        account_swipelayout.stop()

        if (!loaded) {
            account_error_view.visible()
            account_error_view.populate(
                getString(R.string.account_error_get_account_title),
                getString(R.string.account_error_get_account_body)
            )
        } else {
            AlertDialog.Builder(this)
                .setTitle(R.string.account_error_get_account_title)
                .setMessage(R.string.account_error_get_account_body)
                .setPositiveButton(R.string.app_dialog_positive_button, null)
                .create()
                .show()
        }
    }

    override fun showGetBalancesError() {
        account_swipelayout.stop()
        account_error_view.visible()
        account_error_view.populate(
            getString(R.string.account_error_get_balances_title),
            getString(R.string.account_error_get_balances_body)
        )
    }

    override fun navigateToAccountList() {
        model().publish(AccountIntent.BalanceTabIdle)
        startActivity(accountListIntent(this))
        finish()
    }

    override fun navigateToImportKey() {
        model().publish(AccountIntent.BalanceTabIdle)
        startActivity(manageImportKeyIntent(this))
    }

    override fun navigateToCreateAccount() {
        model().publish(AccountIntent.BalanceTabIdle)
        startActivity(manageCreateAccountIntent(this))
    }

    override fun navigateToSettings() {
        model().publish(AccountIntent.BalanceTabIdle)
        startActivity(settingsIntent(this))
    }

    override fun triggerRefresh(page: AccountFragmentPagerAdapter.Page) {
        model().publish(AccountIntent.Refresh(accountBundle))
    }

    private fun publishIdleTab(page: AccountFragmentPagerAdapter.Page): Unit = when (page) {
        AccountFragmentPagerAdapter.Page.BALANCE -> {
            model().publish(AccountIntent.BalanceTabIdle)
        }
        AccountFragmentPagerAdapter.Page.RESOURCES -> {
            model().publish(AccountIntent.ResourceTabIdle)
        }
        AccountFragmentPagerAdapter.Page.VOTE -> {
            model().publish(AccountIntent.VoteTabIdle)
        }
    }

    companion object {

        private const val ACCOUNT_EXTRA = "ACCOUNT_EXTRA"
        private const val PAGE_SELECTION = "PAGE_SELECTION"

        fun accountIntent(
            accountBundle: AccountBundle,
            context: Context,
            page: AccountFragmentPagerAdapter.Page = AccountFragmentPagerAdapter.Page.BALANCE
        ): Intent {
            return with (Intent(context, AccountActivity::class.java)) {
                putExtra(ACCOUNT_EXTRA, accountBundle)
                putExtra(PAGE_SELECTION, page)
                this
            }
        }

        fun accountExtra(intent: Intent): AccountBundle =
            intent.getParcelableExtra(ACCOUNT_EXTRA) as AccountBundle

        fun pageExtra(intent: Intent): AccountFragmentPagerAdapter.Page =
            intent.getSerializableExtra(PAGE_SELECTION) as AccountFragmentPagerAdapter.Page
    }
}