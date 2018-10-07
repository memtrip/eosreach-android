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
package com.memtrip.eosreach.app.account.actions

import android.content.Intent

import android.os.Bundle
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.actions.AccountActionList
import com.memtrip.eosreach.api.actions.model.AccountAction
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountTheme

import com.memtrip.eosreach.app.account.actions.ViewTransferActionActivity.Companion.viewTransferActionIntent
import com.memtrip.eosreach.app.transfer.form.TransferFormActivity.Companion.transferFormIntent
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.start
import com.memtrip.eosreach.uikit.stop
import com.memtrip.eosreach.uikit.visible
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_actions_activity.*
import javax.inject.Inject

abstract class ActionsActivity
    : MviActivity<ActionsIntent, ActionsRenderAction, ActionsViewState, ActionsViewLayout>(), ActionsViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ActionsViewRenderer

    abstract fun accountTheme(): AccountTheme

    private lateinit var adapter: AccountActionsAdapter
    private lateinit var contractAccountBalance: ContractAccountBalance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_actions_activity)

        contractAccountBalance = actionsExtra(intent)

        val adapterInteraction: PublishSubject<Interaction<AccountAction>> = PublishSubject.create()
        adapter = AccountActionsAdapter(this, adapterInteraction)
        account_actions_list_recyclerview.adapter = adapter

        setSupportActionBar(account_actions_toolbar)
        supportActionBar!!.title = contractAccountBalance.accountName
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<ActionsIntent> = Observable.mergeArray(
        Observable.just(ActionsIntent.Init(contractAccountBalance)),
        account_actions_error_view.retryClick().map {
            ActionsIntent.Retry(contractAccountBalance)
        },
        adapter.interaction.map {
            when (it.id) {
                R.id.account_actions_list_recyclerview -> {
                    ActionsIntent.LoadMoreActions(contractAccountBalance, it.data)
                } else -> {
                ActionsIntent.NavigateToViewAction(it.data)
                }
            }
        },
        RxView.clicks(account_actions_transfer_button).map {
            ActionsIntent.NavigateToTransfer(contractAccountBalance)
        },
        RxSwipeRefreshLayout.refreshes(account_actions_list_swiperefresh).map {
            ActionsIntent.Retry(contractAccountBalance)
        }
    )

    override fun layout(): ActionsViewLayout = this

    override fun model(): ActionsViewModel = getViewModel(viewModelFactory)

    override fun render(): ActionsViewRenderer = render

    override fun showProgress() {
        account_actions_list_swiperefresh.start()
        account_actions_error_view.gone()
        account_actions_list_recyclerview.gone()
    }

    override fun showActions(accountActionList: AccountActionList) {
        account_actions_list_recyclerview.visible()
        account_actions_list_swiperefresh.stop()
        adapter.clear()
        adapter.populate(accountActionList.actions)
    }

    override fun showNoActions() {
        account_actions_list_swiperefresh.stop()
        account_actions_no_results_label.visible()
    }

    override fun showError() {
        account_actions_list_swiperefresh.stop()
        account_actions_error_view.visible()
        account_actions_error_view.populate(
            getString(R.string.account_actions_generic_error_title),
            getString(R.string.account_actions_generic_error_body))
    }

    override fun showLoadMoreProgress() {
    }

    override fun appendMoreActions(accountActionList: AccountActionList) {
        adapter.populate(accountActionList.actions)
    }

    override fun showLoadMoreError() {
    }

    override fun navigateToTransfer(contractAccountBalance: ContractAccountBalance) {
        model().publish(ActionsIntent.Idle)
        startActivity(transferFormIntent(contractAccountBalance, this))
    }

    override fun navigateToViewAction(accountAction: AccountAction) {
        model().publish(ActionsIntent.Idle)
        when (accountAction) {
            is AccountAction.Transfer -> {
                startActivity(viewTransferActionIntent(accountAction, accountTheme(), this))
            }
        }
    }

    companion object {

        const val CONTRACT_ACCOUNT_BALANCE_EXTRA = "CONTRACT_ACCOUNT_BALANCE_EXTRA"

        fun actionsExtra(intent: Intent): ContractAccountBalance {
            return intent.getParcelableExtra(CONTRACT_ACCOUNT_BALANCE_EXTRA) as ContractAccountBalance
        }
    }
}
