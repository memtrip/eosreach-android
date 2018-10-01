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
package com.memtrip.eosreach.app.welcome

import android.content.Context
import android.content.Intent
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
import kotlinx.android.synthetic.main.welcome_entry_activity.*
import javax.inject.Inject

class EntryActivity
    : MviActivity<EntryIntent, AccountListRenderAction, EntryViewState, AccountListViewLayout>(), AccountListViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: AccountListViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_entry_activity)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<EntryIntent> = Observable.just(EntryIntent.Init)

    override fun layout(): AccountListViewLayout = this

    override fun model(): EntryViewModel = getViewModel(viewModelFactory)

    override fun render(): AccountListViewRenderer = render

    override fun showProgress() {
        welcome_entry_progressbar.visible()
    }

    override fun showError() {
        welcome_entry_progressbar.gone()
        welcome_entry_database_error.visible()
    }

    override fun showRsaEncryptionFailed() {
        welcome_entry_progressbar.gone()
        welcome_entry_rsa_error.visible()
    }

    override fun navigateToSplash() {
        welcome_entry_progressbar.gone()
        startActivity(splashIntent(this))
        finish()
    }

    override fun navigateToAccount(accountEntity: AccountEntity) {
        welcome_entry_progressbar.gone()
        startActivity(
            accountIntent(
                AccountBundle(accountEntity.accountName),
                this
            )
        )
        finish()
    }

    override fun navigateToAccountList() {
        welcome_entry_progressbar.gone()
        startActivity(accountListIntent(this))
        finish()
    }

    companion object {

        fun entryIntent(context: Context): Intent {
            return with (Intent(context, EntryActivity::class.java)) {
                addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
    }
}
