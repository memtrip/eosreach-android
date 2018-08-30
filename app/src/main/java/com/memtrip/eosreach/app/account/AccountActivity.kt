package com.memtrip.eosreach.app.account

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.os.Bundle
import android.view.Menu
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.accountlist.AccountListActivity.Companion.accountListIntent
import com.memtrip.eosreach.app.manage.ManageNavigationActivity
import com.memtrip.eosreach.app.manage.ManageNavigationActivity.Companion.manageNavigationIntent
import com.memtrip.eosreach.app.settings.SettingsActivity.Companion.settingsIntent
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.account_activity.*
import javax.inject.Inject

class AccountActivity
    : MviActivity<AccountIntent, AccountRenderAction, AccountViewState, AccountViewLayout>(), AccountViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: AccountViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_activity)
        account_toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.account_overflow_menu)
        account_toolbar.title = ""
        setSupportActionBar(account_toolbar)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<AccountIntent> = Observable.mergeArray(
        Observable.just(AccountIntent.Init(accountExtra(intent))),
        account_error_view.retryClick().map { AccountIntent.Retry(accountExtra(intent)) },
        RxView.clicks(account_toolbar_account_name).map { AccountIntent.NavigateToAccountList }
    )

    override fun layout(): AccountViewLayout = this

    override fun model(): AccountViewModel = getViewModel(viewModelFactory)

    override fun render(): AccountViewRenderer = render

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.account_menu, menu)

        menu.findItem(R.id.account_menu_refresh_accounts).setOnMenuItemClickListener {
            model().publish(AccountIntent.RefreshAccounts)
            true
        }

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

    override fun showProgress(accountName: String) {
        account_toolbar_account_name.text = accountName
        account_progressbar.visible()
        account_error_view.gone()
    }

    override fun populate(accountView: AccountView) {
        account_toolbar_account_name.text = accountView.eosAccount!!.accountName
        account_progressbar.gone()
        account_header_group.visible()

        val accountPagerFragment = AccountPagerFragment(
            supportFragmentManager,
            this,
            accountView)
        account_viewpager.adapter = accountPagerFragment
        account_viewpager.offscreenPageLimit = 2
        account_tablayout.setupWithViewPager(account_viewpager)

        account_viewpager.visible()
    }

    override fun showGetAccountError() {
        account_progressbar.gone()
        account_error_view.visible()
        account_error_view.populate(
            getString(R.string.account_error_get_account_title),
            getString(R.string.account_error_get_account_body)
        )
    }

    override fun showGetBalancesError() {
        account_progressbar.gone()
        account_error_view.visible()
        account_error_view.populate(
            getString(R.string.account_error_get_balances_title),
            getString(R.string.account_error_get_balances_body)
        )
    }

    override fun navigateToAccountList() {
        model().publish(AccountIntent.Idle)
        val intent = accountListIntent(this)
        startActivity(intent)
    }

    override fun navigateToImportKey() {
        model().publish(AccountIntent.Idle)
        startActivity(manageNavigationIntent(
            ManageNavigationActivity.Screen.IMPORT_KEY, this))
    }

    override fun navigateToCreateAccount() {
        model().publish(AccountIntent.Idle)
        startActivity(manageNavigationIntent(
            ManageNavigationActivity.Screen.CREATE_ACCOUNT, this))
    }

    override fun navigateToSettings() {
        model().publish(AccountIntent.Idle)
        startActivity(settingsIntent(this))
    }

    companion object {

        private const val ACCOUNT_EXTRA = "ACCOUNT_EXTRA"

        fun accountIntent(accountBundle: AccountBundle, context: Context): Intent {
            return with (Intent(context, AccountActivity::class.java)) {
                putExtra(ACCOUNT_EXTRA, accountBundle)
                flags = FLAG_ACTIVITY_SINGLE_TOP or FLAG_ACTIVITY_CLEAR_TOP
                this
            }
        }

        fun accountExtra(intent: Intent): AccountBundle {
            return intent.getParcelableExtra(ACCOUNT_EXTRA) as AccountBundle
        }
    }
}