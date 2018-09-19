package com.memtrip.eosreach.app.account.balance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.AccountBalanceList
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.actions.ActionsActivity.Companion.actionsIntent
import com.memtrip.eosreach.app.manage.ManageCreateAccountActivity.Companion.manageCreateAccountIntent
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.gone
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
    private lateinit var accountName: String
    private lateinit var accountBalanceList: AccountBalanceList

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.account_balance_fragment, container, false)

        accountName = accountName(arguments!!)
        accountBalanceList = accountBalanceListExtra(arguments!!)

        val adapterInteraction: PublishSubject<Interaction<ContractAccountBalance>> = PublishSubject.create()
        adapter = AccountBalanceListAdapter(context!!, adapterInteraction)
        view.balance_list_recyclerview.adapter = adapter

        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<BalanceIntent> = Observable.merge(
        Observable.just(BalanceIntent.Init(accountBalanceList)),
        RxView.clicks(balance_airdrop_button).map { BalanceIntent.ScanForAirdropTokens(accountName) },
        RxView.clicks(balance_create_account).map { BalanceIntent.NavigateToCreateAccount },
        adapter.interaction.map { BalanceIntent.NavigateToActions(it.data) }
    )

    override fun layout(): BalanceViewLayout = this

    override fun model(): BalanceViewModel = getViewModel(viewModelFactory)

    override fun render(): BalanceViewRenderer = render

    override fun showBalances(accountBalanceList: AccountBalanceList) {
        balance_list_recyclerview.visible()
        balance_airdrop_progress_group.gone()
        adapter.clear()
        adapter.populate(accountBalanceList.balances)
    }

    override fun showEmptyBalance() {
        balance_token_title.gone()
        balance_list_recyclerview.gone()
        balance_airdrop_progress_group.gone()
        balance_empty_group.visible()
    }

    override fun showAirdropError(message: String) {
        balance_list_recyclerview.gone()
        balance_airdrop_progress_group.gone()
    }

    override fun showAirdropProgress() {
        balance_list_recyclerview.gone()
        balance_airdrop_progress_group.visible()
    }

    override fun navigateToCreateAccount() {
        model().publish(BalanceIntent.Idle)
        startActivity(manageCreateAccountIntent(context!!))
    }

    override fun navigateToActions(contractAccountBalance: ContractAccountBalance) {
        model().publish(BalanceIntent.Idle)
        startActivity(actionsIntent(contractAccountBalance, context!!))
    }

    companion object {

        private const val ACCOUNT_NAME = "ACCOUNT_NAME"
        private const val ACCOUNT_BALANCES = "ACCOUNT_BALANCES"

        fun newInstance(
            accountName: String,
            accountBalances: AccountBalanceList
        ): BalanceFragment = with (BalanceFragment()) {
            arguments = toBundle(accountName, accountBalances)
            this
        }

        private fun toBundle(
            accountName: String,
            accountBalances: AccountBalanceList
        ): Bundle = with (Bundle()) {
            putString(ACCOUNT_NAME, accountName)
            putParcelable(ACCOUNT_BALANCES, accountBalances)
            this
        }

        private fun accountBalanceListExtra(bundle: Bundle): AccountBalanceList =
            bundle.getParcelable(ACCOUNT_BALANCES)

        private fun accountName(bundle: Bundle): String =
            bundle.getString(ACCOUNT_NAME)
    }
}
