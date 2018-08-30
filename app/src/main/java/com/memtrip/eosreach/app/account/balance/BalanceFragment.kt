package com.memtrip.eosreach.app.account.balance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.AccountBalances
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.manage.ManageNavigationActivity
import com.memtrip.eosreach.app.manage.ManageNavigationActivity.Companion.manageNavigationIntent
import com.memtrip.eosreach.uikit.visible
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.account_balance_fragment.*
import javax.inject.Inject

class BalanceFragment
    : MviFragment<BalanceIntent, BalanceRenderAction, BalanceViewState, BalanceViewLayout>(), BalanceViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: BalanceViewRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.account_balance_fragment, container, false)
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<BalanceIntent> = Observable.merge(
        Observable.just(BalanceIntent.Init(fromBundle(arguments!!))),
        RxView.clicks(balance_create_account).map { BalanceIntent.NavigateToCreateAccount }
    )

    override fun layout(): BalanceViewLayout = this

    override fun model(): BalanceViewModel = getViewModel(viewModelFactory)

    override fun render(): BalanceViewRenderer = render

    override fun showBalances(balances: AccountBalances) {
        print("")
    }

    override fun showEmptyBalance() {
        balance_empty_group.visible()
    }

    override fun navigateToCreateAccount() {
        model().publish(BalanceIntent.Idle)
        startActivity(manageNavigationIntent(ManageNavigationActivity.Screen.CREATE_ACCOUNT, context!!))
    }

    companion object {
        fun newInstance(accountBalances: AccountBalances): BalanceFragment = with (BalanceFragment()) {
            arguments = toBundle(accountBalances)
            this
        }

        private fun toBundle(accountBalances: AccountBalances): Bundle = with (Bundle()) {
            putParcelable("accountBalances", accountBalances)
            this
        }

        private fun fromBundle(bundle: Bundle): AccountBalances = bundle.getParcelable("accountBalances")
    }
}
