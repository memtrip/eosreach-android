package com.memtrip.eosreach.app.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding2.support.v4.view.RxViewPager
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
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

    lateinit var accountBundle: AccountBundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_activity)
        account_toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.account_overflow_menu)
        account_toolbar.title = ""
        setSupportActionBar(account_toolbar)
        accountBundle = accountExtra(intent)
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
        Observable.just(AccountIntent.Init(accountBundle)),
        account_error_view.retryClick().map { AccountIntent.Retry(accountBundle) },
        RxView.clicks(account_toolbar_account_name).map { AccountIntent.NavigateToAccountList },
        RxViewPager.pageSelections(account_viewpager).map { position ->
            when (position) {
                AccountPagerFragment.Page.BALANCE.ordinal -> AccountIntent.BalanceTabIdle
                AccountPagerFragment.Page.RESOURCES.ordinal -> AccountIntent.ResourceTabIdle
                AccountPagerFragment.Page.VOTE.ordinal -> AccountIntent.VoteTabIdle
                else -> AccountIntent.BalanceTabIdle
            }
        }
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

    override fun populate(accountView: AccountView, page: AccountPagerFragment.Page) {
        model().publish(AccountIntent.BalanceTabIdle)

        account_toolbar_account_name.text = accountView.eosAccount!!.accountName
        account_swipelayout.stop()
        account_header_group.visible()

        val accountPagerFragment = AccountPagerFragment(
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
                    AccountPagerFragment.Page.BALANCE.ordinal -> {
                        model().publish(AccountIntent.BalanceTabIdle)
                    }
                    AccountPagerFragment.Page.RESOURCES.ordinal -> {
                        model().publish(AccountIntent.ResourceTabIdle)
                    }
                    AccountPagerFragment.Page.VOTE.ordinal -> {
                        model().publish(AccountIntent.VoteTabIdle)
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
        account_error_view.visible()
        account_error_view.populate(
            getString(R.string.account_error_get_account_title),
            getString(R.string.account_error_get_account_body)
        )
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

    override fun triggerRefresh(page: AccountPagerFragment.Page) {
        model().publish(AccountIntent.Refresh(accountBundle))
    }

    companion object {

        private const val ACCOUNT_EXTRA = "ACCOUNT_EXTRA"

        fun accountIntent(accountBundle: AccountBundle, context: Context): Intent {
            return with (Intent(context, AccountActivity::class.java)) {
                putExtra(ACCOUNT_EXTRA, accountBundle)
                this
            }
        }

        fun accountExtra(intent: Intent): AccountBundle {
            return intent.getParcelableExtra(ACCOUNT_EXTRA) as AccountBundle
        }
    }
}