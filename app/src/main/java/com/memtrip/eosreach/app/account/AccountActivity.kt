package com.memtrip.eosreach.app.account

import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat

import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.balance.AccountBalances
import com.memtrip.eosreach.app.MviActivity

import com.memtrip.eosreach.app.ViewModelFactory
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

    override fun intents(): Observable<AccountIntent> = Observable.merge(
        Observable.just(AccountIntent.Init),
        account_error_view.retryClick().map { AccountIntent.Retry }
    )

    override fun layout(): AccountViewLayout = this

    override fun model(): AccountViewModel = getViewModel(viewModelFactory)

    override fun render(): AccountViewRenderer = render

    override fun showProgress() {
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

    override fun showGetAccountError(accountName: String) {
        account_toolbar_account_name.text = accountName
        account_progressbar.gone()
        account_error_view.visible()
        account_error_view.populate(
            getString(R.string.account_error_get_account_title),
            getString(R.string.account_error_get_account_body)
        )
    }

    override fun showGetBalancesError(accountName: String) {
        account_toolbar_account_name.text = accountName
        account_progressbar.gone()
        account_error_view.visible()
        account_error_view.populate(
            getString(R.string.account_error_get_balances_title),
            getString(R.string.account_error_get_balances_body)
        )
    }
}