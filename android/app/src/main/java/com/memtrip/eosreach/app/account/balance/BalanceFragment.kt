package com.memtrip.eosreach.app.account.balance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.AccountBalance
import com.memtrip.eosreach.api.balance.AccountBalanceList

import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.actions.ActionsActivity.Companion.actionsIntent
import com.memtrip.eosreach.app.manage.ManageCreateAccountActivity.Companion.manageCreateAccountIntent

import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.visible
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_balance_fragment.*
import kotlinx.android.synthetic.main.account_balance_fragment.view.*

import javax.inject.Inject

class BalanceFragment
    : MviFragment<BalanceIntent, BalanceRenderAction, BalanceViewState, BalanceViewLayout>(), BalanceViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: BalanceViewRenderer

    private lateinit var adapter: AccountBalanceListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.account_balance_fragment, container, false)

        val adapterInteraction: PublishSubject<Interaction<AccountBalance>> = PublishSubject.create()
        adapter = AccountBalanceListAdapter(context!!, adapterInteraction)
        view.balance_list_recyclerview.adapter = adapter

        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<BalanceIntent> = Observable.merge(
        Observable.just(BalanceIntent.Init(fromBundle(arguments!!))),
        RxView.clicks(balance_create_account).map { BalanceIntent.NavigateToCreateAccount },
        adapter.interaction.map { BalanceIntent.NavigateToActions(it.data) }
    )

    override fun layout(): BalanceViewLayout = this

    override fun model(): BalanceViewModel = getViewModel(viewModelFactory)

    override fun render(): BalanceViewRenderer = render

    override fun showBalances(accountBalanceList: AccountBalanceList) {
        adapter.clear()
        adapter.populate(accountBalanceList.balances)
    }

    override fun showEmptyBalance() {
        balance_empty_group.visible()
    }

    override fun navigateToCreateAccount() {
        model().publish(BalanceIntent.Idle)
        startActivity(manageCreateAccountIntent(context!!))
    }

    override fun navigateToActions(accountBalance: AccountBalance) {
        model().publish(BalanceIntent.Idle)
        startActivity(actionsIntent(accountBalance, context!!))
    }

    companion object {
        fun newInstance(accountBalances: AccountBalanceList): BalanceFragment = with (BalanceFragment()) {
            arguments = toBundle(accountBalances)
            this
        }

        private fun toBundle(accountBalances: AccountBalanceList): Bundle = with (Bundle()) {
            putParcelable("accountBalances", accountBalances)
            this
        }

        private fun fromBundle(bundle: Bundle): AccountBalanceList = bundle.getParcelable("accountBalances")
    }
}
