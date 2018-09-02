package com.memtrip.eosreach.app.account.actions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.AccountBalance
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import dagger.android.AndroidInjection
import io.reactivex.Observable
import javax.inject.Inject

class ActionsActivity
    : MviActivity<ActionsIntent, ActionsRenderAction, ActionsViewState, ActionsViewLayout>(), ActionsViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ActionsViewRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actions_activity)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<ActionsIntent> {
        return Observable.just(ActionsIntent.Init(actionsExtra(intent)))
    }

    override fun layout(): ActionsViewLayout = this

    override fun model(): ActionsViewModel = getViewModel(viewModelFactory)

    override fun render(): ActionsViewRenderer = render

    override fun showProgress() {
    }

    override fun showError() {
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
