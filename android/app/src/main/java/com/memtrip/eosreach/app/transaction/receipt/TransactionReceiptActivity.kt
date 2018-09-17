package com.memtrip.eosreach.app.transaction.receipt

import android.content.Context
import android.content.Intent
import android.net.Uri

import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView

import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.api.transfer.ActionReceipt
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountActivity.Companion.accountIntent
import com.memtrip.eosreach.app.account.AccountBundle
import com.memtrip.eosreach.app.account.AccountFragmentPagerAdapter
import com.memtrip.eosreach.app.account.actions.ActionsActivity.Companion.actionsIntent

import dagger.android.AndroidInjection

import io.reactivex.Observable
import kotlinx.android.synthetic.main.transaction_receipt_activity.*
import javax.inject.Inject

class TransactionReceiptActivity
    : MviActivity<TransactionReceiptIntent, TransferReceiptRenderAction, TransactionReceiptViewState, TransferReceiptViewLayout>(), TransferReceiptViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: TransferReceiptViewRenderer

    private lateinit var actionReceipt: ActionReceipt
    private lateinit var transactionReceiptRoute: TransactionReceiptRoute

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transaction_receipt_activity)
        actionReceipt = actionReceiptExtra(intent)
        transactionReceiptRoute = transactionReceiptRouteExtra(intent)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<TransactionReceiptIntent> = Observable.merge(
        RxView.clicks(transaction_receipt_done_button).map {
            navigateToRoute(transactionReceiptRoute)
        },
        RxView.clicks(transaction_receipt_view_block_explorer_button).map {
            TransactionReceiptIntent.NavigateToBlockExplorer(actionReceipt.transactionId)
        }
    )

    override fun layout(): TransferReceiptViewLayout = this

    override fun model(): TransactionReceiptViewModel = getViewModel(viewModelFactory)

    override fun render(): TransferReceiptViewRenderer = render

    override fun populate(actionReceipt: ActionReceipt) {

    }

    override fun navigateToBlockExplorer(transactionId: String) {
        model().publish(TransactionReceiptIntent.Idle)
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
            getString(R.string.transaction_view_confirm_block_explorer_url, transactionId))))
    }

    override fun navigateToActions() {
        val contractAccountBalance = contractAccountBalanceExtra(intent)
        startActivities(
            arrayOf(
                with(accountIntent(AccountBundle(
                    contractAccountBalance.accountName
                ), this)) {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK)
                    this
                },
                actionsIntent(
                    contractAccountBalance,
                    this)
            )
        )
    }

    override fun navigateToAccount() {
        startActivity(with(accountIntent(AccountBundle(
            actionReceipt.authorizingAccountName
        ), this, AccountFragmentPagerAdapter.Page.RESOURCES)) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_NEW_TASK)
            this
        })
    }

    override fun onBackPressed() {
        navigateToRoute(transactionReceiptRoute)
    }

    private fun navigateToRoute(transactionReceiptRoute: TransactionReceiptRoute): TransactionReceiptIntent = when (transactionReceiptRoute) {
        TransactionReceiptRoute.ACTIONS -> {
            TransactionReceiptIntent.NavigateToActions
        }
        TransactionReceiptRoute.ACCOUNT -> {
            TransactionReceiptIntent.NavigateToAccount
        }
    }

    companion object {

        private const val ACTION_RECEIPT = "ACTION_RECEIPT"
        private const val CONTRACT_ACCOUNT_BALANCE = "CONTRACT_ACCOUNT_BALANCE"
        private const val TRANSACTION_RECEIPT_ROUTE = "TRANSACTION_RECEIPT_ROUTE"

        fun transactionReceiptIntent(
            actionReceipt: ActionReceipt,
            contractAccountBalance: ContractAccountBalance,
            transactionReceiptRoute: TransactionReceiptRoute,
            context: Context
        ): Intent {
            return with (Intent(context, TransactionReceiptActivity::class.java)) {
                putExtra(ACTION_RECEIPT, actionReceipt)
                putExtra(CONTRACT_ACCOUNT_BALANCE, contractAccountBalance)
                putExtra(TRANSACTION_RECEIPT_ROUTE, transactionReceiptRoute)
                this
            }
        }

        fun actionReceiptExtra(intent: Intent): ActionReceipt =
            intent.getParcelableExtra(ACTION_RECEIPT)

        private fun contractAccountBalanceExtra(intent: Intent): ContractAccountBalance =
            intent.getParcelableExtra(CONTRACT_ACCOUNT_BALANCE)

        fun transactionReceiptRouteExtra(intent: Intent) : TransactionReceiptRoute =
            intent.getSerializableExtra(TRANSACTION_RECEIPT_ROUTE) as TransactionReceiptRoute
    }
}
