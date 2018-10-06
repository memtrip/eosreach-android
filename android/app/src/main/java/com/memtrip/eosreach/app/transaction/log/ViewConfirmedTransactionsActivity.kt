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
import android.net.Uri
import android.os.Bundle
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.db.transaction.TransactionLogEntity
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.transaction_view_confirmed_activity.*
import javax.inject.Inject

class ViewConfirmedTransactionsActivity
    : MviActivity<ViewConfirmedTransactionsIntent, ViewConfirmedTransactionsRenderAction, ViewConfirmedTransactionsViewState, ViewConfirmedTransactionsViewLayout>(), ViewConfirmedTransactionsViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ViewConfirmedTransactionsViewRenderer

    private lateinit var adapter: ViewConfirmedTransactionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transaction_view_confirmed_activity)
        setSupportActionBar(transaction_view_confirmed_toolbar)
        supportActionBar!!.title = getString(R.string.settings_view_confirmed_transactions)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val adapterInteraction: PublishSubject<Interaction<TransactionLogEntity>> = PublishSubject.create()
        adapter = ViewConfirmedTransactionsAdapter(this, adapterInteraction)
        transaction_view_confirmed_recyclerview.adapter = adapter
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<ViewConfirmedTransactionsIntent> = Observable.merge(
        Observable.just(ViewConfirmedTransactionsIntent.Init),
        adapter.interaction.map { ViewConfirmedTransactionsIntent.NavigateToBlockExplorer(it.data.transactionId) }
    )

    override fun layout(): ViewConfirmedTransactionsViewLayout = this

    override fun model(): ViewConfirmedTransactionsViewModel = getViewModel(viewModelFactory)

    override fun render(): ViewConfirmedTransactionsViewRenderer = render

    override fun showProgress() {
        transaction_view_confirmed_progress.visible()
        transaction_view_confirmed_error.gone()
        transaction_view_confirmed_recyclerview.gone()
    }

    override fun showError() {
        transaction_view_confirmed_progress.gone()
        transaction_view_confirmed_error.text = getString(R.string.transaction_view_confirmed_error)
        transaction_view_confirmed_error.visible()
    }

    override fun populate(transactionLogEntities: List<TransactionLogEntity>) {
        transaction_view_confirmed_progress.gone()
        transaction_view_confirmed_recyclerview.visible()
        adapter.clear()
        adapter.populate(transactionLogEntities)
    }

    override fun empty() {
        transaction_view_confirmed_progress.gone()
        transaction_view_confirmed_empty.text = getString(R.string.transaction_view_confirmed_empty)
        transaction_view_confirmed_empty.visible()
    }

    override fun navigateToBlockExplorer(transactionId: String) {
        model().publish(ViewConfirmedTransactionsIntent.Idle)
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
            getString(R.string.transaction_view_confirm_block_explorer_url, transactionId))))
    }

    companion object {

        fun viewConfirmedTransactionsIntent(context: Context): Intent {
            return Intent(context, ViewConfirmedTransactionsActivity::class.java)
        }
    }
}
