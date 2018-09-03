package com.memtrip.eosreach.app.welcome

import android.os.Bundle
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountActivity.Companion.accountIntent
import com.memtrip.eosreach.app.account.AccountBundle
import com.memtrip.eosreach.app.accountlist.AccountListActivity.Companion.accountListIntent
import com.memtrip.eosreach.app.welcome.splash.SplashActivity.Companion.splashIntent
import com.memtrip.eosreach.db.account.AccountEntity

import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.welcome_account_list_activity.*
import javax.inject.Inject

class EntryActivity
    : MviActivity<EntryIntent, AccountListRenderAction, EntryViewState, AccountListViewLayout>(), AccountListViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: AccountListViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_account_list_activity)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<EntryIntent> = Observable.just(EntryIntent.Init)

    override fun layout(): AccountListViewLayout = this

    override fun model(): EntryViewModel = getViewModel(viewModelFactory)

    override fun render(): AccountListViewRenderer = render

    override fun showProgress() {
        welcome_account_list_progressbar.visible()
    }

    override fun showError() {
        welcome_account_list_progressbar.gone()
    }

    override fun navigateToSplash() {
        welcome_account_list_progressbar.gone()
        startActivity(splashIntent(this))
        finish()
    }

    override fun navigateToAccount(accountEntity: AccountEntity) {
        welcome_account_list_progressbar.gone()
        startActivity(
            accountIntent(
                AccountBundle(
                    accountEntity.accountName,
                    accountEntity.balance,
                    accountEntity.symbol
                ),
                this
            )
        )
        finish()
    }

    override fun navigateToAccountList() {
        welcome_account_list_progressbar.gone()
        startActivity(accountListIntent(this))
        finish()
    }
}
