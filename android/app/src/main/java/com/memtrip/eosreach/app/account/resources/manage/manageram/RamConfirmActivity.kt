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
package com.memtrip.eosreach.app.account.resources.manage.manageram

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.api.transfer.ActionReceipt
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.transaction.log.TransactionLogActivity.Companion.transactionLogIntent
import com.memtrip.eosreach.app.transaction.receipt.TransactionReceiptActivity
import com.memtrip.eosreach.app.transaction.receipt.TransactionReceiptRoute
import com.memtrip.eosreach.app.transfer.confirm.TransferConfirmIntent
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.invisible
import com.memtrip.eosreach.uikit.visible

import dagger.android.AndroidInjection
import io.reactivex.Observable
import javax.inject.Inject

import kotlinx.android.synthetic.main.ram_confirm_activity.*

class RamConfirmActivity
    : MviActivity<RamConfirmIntent, RamConfirmRenderAction, RamConfirmViewState, RamConfirmViewLayout>(), RamConfirmViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: RamConfirmViewRenderer

    private lateinit var kb: String
    private lateinit var ramPricePerKb: Balance
    private lateinit var commitType: RamCommitType
    private lateinit var contractAccountBalance: ContractAccountBalance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ram_confirm_activity)
        kb = kb(intent)
        ramPricePerKb = ramPricePerKb(intent)
        commitType = commitType(intent)
        contractAccountBalance = contractAccountBalance(intent)
        setSupportActionBar(ram_confirm_toolbar)
        supportActionBar!!.title = typeLabel(commitType)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        ram_confirm_cta_button.text = buttonLabel(commitType)
    }

    private fun typeLabel(type: RamCommitType): String = when (type) {
        RamCommitType.BUY -> getString(R.string.resources_ram_confirm_form_buy_label)
        RamCommitType.SELL -> getString(R.string.resources_ram_confirm_form_sell_label)
    }

    private fun buttonLabel(type: RamCommitType): String = when (type) {
        RamCommitType.BUY -> getString(R.string.resources_manage_ram_form_buy_button)
        RamCommitType.SELL -> getString(R.string.resources_manage_ram_form_sell_button)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<RamConfirmIntent> = Observable.merge(
        Observable.just(RamConfirmIntent.Init),
        RxView.clicks(ram_confirm_cta_button).map {
            RamConfirmIntent.Confirm(contractAccountBalance.accountName, kb, commitType)
        }
    )

    override fun layout(): RamConfirmViewLayout = this

    override fun model(): RamConfirmViewModel = getViewModel(viewModelFactory)

    override fun render(): RamConfirmViewRenderer = render

    override fun populate() {
        ram_details_layout.populate(kb, ramPricePerKb)
    }

    override fun showProgress() {
        ram_confirm_cta_progress.visible()
        ram_confirm_cta_button.invisible()
    }

    override fun onSuccess(transferReceipt: ActionReceipt) {
        startActivity(TransactionReceiptActivity.transactionReceiptIntent(
            transferReceipt,
            contractAccountBalance,
            TransactionReceiptRoute.ACCOUNT_RESOURCES,
            this))
        finish()
    }

    override fun showError(log: String) {
        ram_confirm_cta_progress.gone()
        ram_confirm_cta_button.visible()

        AlertDialog.Builder(this)
            .setMessage(getString(R.string.app_dialog_transaction_error_body))
            .setPositiveButton(R.string.transaction_view_log_position_button) { _, _ ->
                model().publish(RamConfirmIntent.Idle)
                startActivity(transactionLogIntent(log, this))
            }
            .setNegativeButton(R.string.transaction_view_log_negative_button, null)
            .create()
            .show()
    }

    companion object {

        private const val KB = "KB"
        private const val RAM_PRICE_PER_KB = "RAM_PRICE_PER_KB"
        private const val COMMIT_TYPE = "COMMIT_TYPE"
        private const val CONTRACT_ACCOUNT_BALANCE = "CONTRACT_ACCOUNT_BALANCE"

        fun ramConfirmIntent(
            kilobytes: String,
            ramPricePerKb: Balance,
            commitType: RamCommitType,
            contractAccountBalance: ContractAccountBalance,
            context: Context
        ): Intent {
            return with(Intent(context, RamConfirmActivity::class.java)) {
                putExtra(KB, kilobytes)
                putExtra(RAM_PRICE_PER_KB, ramPricePerKb)
                putExtra(COMMIT_TYPE, commitType)
                putExtra(CONTRACT_ACCOUNT_BALANCE, contractAccountBalance)
            }
        }

        private fun kb(intent: Intent): String = intent.getStringExtra(KB)

        private fun ramPricePerKb(intent: Intent): Balance = intent.getParcelableExtra(RAM_PRICE_PER_KB)

        private fun commitType(intent: Intent): RamCommitType {
            return intent.getSerializableExtra(COMMIT_TYPE) as RamCommitType
        }

        private fun contractAccountBalance(intent: Intent): ContractAccountBalance {
            return intent.getParcelableExtra(CONTRACT_ACCOUNT_BALANCE)
        }
    }
}
