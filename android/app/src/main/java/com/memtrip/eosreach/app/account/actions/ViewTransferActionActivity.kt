package com.memtrip.eosreach.app.account.actions

import android.content.Context
import android.content.Intent
import android.os.Bundle

import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.actions.model.AccountAction
import com.memtrip.eosreach.app.ViewModelFactory

import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.account_actions_view_transfer.*
import javax.inject.Inject


class ViewTransferActionActivity
    : MviActivity<ViewTransferActionIntent, ViewTransferActionRenderAction, ViewTransferActionViewState, ViewTransferActionViewLayout>(), ViewTransferActionViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ViewTransferActionViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_actions_view_transfer)
        setSupportActionBar(account_actions_view_transfer_action_toolbar)
        supportActionBar!!.title = getString(R.string.account_actions_view_transfer_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<ViewTransferActionIntent> {
        return Observable.just(ViewTransferActionIntent.Init(actionsExtra(intent)))
    }

    override fun layout(): ViewTransferActionViewLayout = this

    override fun model(): ViewTransferActionViewModel = getViewModel(viewModelFactory)

    override fun render(): ViewTransferActionViewRenderer = render

    override fun populate(accountAction: AccountAction.Transfer) {

        account_actions_view_transfer_details.populate(
            accountAction.quantity,
            accountAction.to,
            accountAction.from,
            accountAction.memo,
            accountAction.contractAccountBalance)

        account_actions_view_transfer_details.populateDate(
            accountAction.formattedDate)
    }

    companion object {

        private const val ACCOUNT_ACTION_TRANSFER = "ACCOUNT_ACTION_TRANSFER"

        fun viewTransferActionIntent(accountAction: AccountAction.Transfer, context: Context): Intent {
            return with (Intent(context, ViewTransferActionActivity::class.java)) {
                putExtra(ACCOUNT_ACTION_TRANSFER, accountAction)
                this
            }
        }

        fun actionsExtra(intent: Intent): AccountAction.Transfer {
            return intent.getParcelableExtra(ACCOUNT_ACTION_TRANSFER) as AccountAction.Transfer
        }
    }
}