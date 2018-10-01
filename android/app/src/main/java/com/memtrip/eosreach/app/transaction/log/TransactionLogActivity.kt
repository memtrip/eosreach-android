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
package com.memtrip.eosreach.app.transaction.log

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.transaction_log_activity.*
import javax.inject.Inject

internal class TransactionLogActivity
    : MviActivity<TransactionLogIntent, TransactionLogRenderAction, TransactionLogViewState, TransactionLogViewLayout>(), TransactionLogViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: TransactionLogViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transaction_log_activity)
        setSupportActionBar(transaction_log_toolbar)
        supportActionBar!!.title = getString(R.string.transaction_view_log_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<TransactionLogIntent> {
        return Observable.just(TransactionLogIntent.ShowLog(transactionLogExtra(intent)))
    }

    override fun layout(): TransactionLogViewLayout = this

    override fun model(): TransactionLogViewModel = getViewModel(viewModelFactory)

    override fun render(): TransactionLogViewRenderer = render

    override fun showLog(log: String) {
        transaction_log_data.text = log
    }

    companion object {

        private const val TRANSACTION_LOG = "TRANSACTION_LOG"

        fun transactionLogIntent(log: String, context: Context): Intent {
            return with (Intent(context, TransactionLogActivity::class.java)) {
                putExtra(TRANSACTION_LOG, log)
                this
            }
        }

        fun transactionLogExtra(intent: Intent): String {
            return intent.getStringExtra(TRANSACTION_LOG) as String
        }
    }
}
