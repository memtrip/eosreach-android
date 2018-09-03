package com.memtrip.eosreach.app.account.actions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.actions.AccountActionList
import com.memtrip.eosreach.api.actions.model.AccountAction
import com.memtrip.eosreach.api.balance.AccountBalance
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_actions_activity.*
import javax.inject.Inject

class ActionsActivity
    : MviActivity<ActionsIntent, ActionsRenderAction, ActionsViewState, ActionsViewLayout>(), ActionsViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ActionsViewRenderer

    private lateinit var adapter: AccountActionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_actions_activity)

        val adapterInteraction: PublishSubject<Interaction<AccountAction>> = PublishSubject.create()
        adapter = AccountActionsAdapter(this, adapterInteraction)
        account_actions_list_recyclerview.adapter = adapter
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<ActionsIntent> = Observable.merge(
        Observable.just(ActionsIntent.Init(actionsExtra(intent))),
        account_actions_error_view.retryClick().map {
            ActionsIntent.Retry(actionsExtra(intent))
        },
        adapter.interaction.map {
            ActionsIntent.NavigateToViewAction(it.data)
        }
    )

    override fun layout(): ActionsViewLayout = this

    override fun model(): ActionsViewModel = getViewModel(viewModelFactory)

    override fun render(): ActionsViewRenderer = render

    override fun showProgress() {
        account_actions_progress.visible()
    }

    override fun showActions(accountActionList: AccountActionList) {
        account_actions_progress.gone()
        account_actions_list_recyclerview.visible()
        adapter.populate(accountActionList.actions)
    }

    override fun showNoActions() {
        account_actions_progress.gone()
        account_actions_error_view.populate(
            getString(R.string.account_actions_empty_title),
            getString(R.string.account_actions_empty_body))
    }

    override fun showError() {
        account_actions_progress.gone()
        account_actions_error_view.populate(
            getString(R.string.account_actions_generic_error_title),
            getString(R.string.account_actions_generic_error_body))
    }

    override fun navigateToViewAction(accountAction: AccountAction) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {

        private const val ACCOUNT_BALANCE_EXTRA = "ACCOUNT_BALANCE_EXTRA"

        fun actionsIntent(accountBalance: AccountBalance, context: Context): Intent {
            return with (Intent(context, ActionsActivity::class.java)) {
                putExtra(ACCOUNT_BALANCE_EXTRA, accountBalance)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                this
            }
        }

        fun actionsExtra(intent: Intent): AccountBalance {
            return intent.getParcelableExtra(ACCOUNT_BALANCE_EXTRA) as AccountBalance
        }
    }
}
