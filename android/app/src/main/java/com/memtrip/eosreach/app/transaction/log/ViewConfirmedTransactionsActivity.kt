package com.memtrip.eosreach.app.transaction.log

import android.os.Bundle
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.balance.AccountBalanceListAdapter
import com.memtrip.eosreach.db.transaction.TransactionLogEntity
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible

import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_balance_fragment.view.*
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
        supportActionBar!!.title = getString(R.string.settings_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val adapterInteraction: PublishSubject<Interaction<TransactionLogEntity>> = PublishSubject.create()
        adapter = ViewConfirmedTransactionsAdapter(this, adapterInteraction)
        transaction_view_confirmed_recyclerview.adapter = adapter
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<ViewConfirmedTransactionsIntent> = Observable.just(ViewConfirmedTransactionsIntent.Init)

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
}
