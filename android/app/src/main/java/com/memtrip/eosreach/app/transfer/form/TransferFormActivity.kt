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
package com.memtrip.eosreach.app.transfer.form

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.transfer.confirm.TransferConfirmActivity.Companion.transferConfirmIntent
import com.memtrip.eosreach.uikit.inputfilter.AccountNameInputFilter
import com.memtrip.eosreach.uikit.inputfilter.CurrencyFormatInputFilter
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.transfer_form_activity.*
import javax.inject.Inject

class TransferFormActivity
    : MviActivity<TransferFormIntent, TransferFormRenderAction, TransferFormViewState, TransferFormViewLayout>(), TransferFormViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: TransferFormViewRenderer

    lateinit var contractAccountBalance: ContractAccountBalance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transfer_form_activity)
        setSupportActionBar(transfer_form_toolbar)
        supportActionBar!!.title = getString(R.string.transfer_form_toolbar_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        transfer_form_to_input.filters = arrayOf(
            AccountNameInputFilter(),
            InputFilter.LengthFilter(resources.getInteger(R.integer.app_account_name_length)))
        transfer_form_amount_input.filters = arrayOf(
            CurrencyFormatInputFilter(),
            InputFilter.LengthFilter(resources.getInteger(R.integer.transfer_amount_length)))

        contractAccountBalance = transferExtra(intent)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<TransferFormIntent> = Observable.merge(
        Observable.just(TransferFormIntent.Init(contractAccountBalance)),
        Observable.merge(
            RxView.clicks(transfer_form_next_button),
            RxTextView.editorActions(transfer_form_memo_input)
        ).map {
            hideKeyboard()
            TransferFormIntent.SubmitForm(
                TransferFormData(
                    contractAccountBalance,
                    transfer_form_to_input.text.toString(),
                    transfer_form_amount_input.text.toString(),
                    transfer_form_memo_input.text.toString()
                )
            )
        }
    )

    override fun layout(): TransferFormViewLayout = this

    override fun model(): TransferFormViewModel = getViewModel(viewModelFactory)

    override fun render(): TransferFormViewRenderer = render

    override fun populate(formattedBalance: String) {
        transfer_form_amount_label.text = getString(R.string.transfer_form_amount_label, formattedBalance)
    }

    override fun showValidationError(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(R.string.app_dialog_positive_button, null)
            .create()
            .show()
    }

    override fun navigateToConfirmation(transferFormData: TransferFormData) {
        model().publish(TransferFormIntent.Idle)
        startActivity(transferConfirmIntent(transferFormData, this))
    }

    companion object {

        private const val ACCOUNT_BALANCE_EXTRA = "ACCOUNT_BALANCE_EXTRA"

        fun transferFormIntent(contractAccountBalance: ContractAccountBalance, context: Context): Intent {
            return with (Intent(context, TransferFormActivity::class.java)) {
                putExtra(ACCOUNT_BALANCE_EXTRA, contractAccountBalance)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                this
            }
        }

        fun transferExtra(intent: Intent): ContractAccountBalance {
            return intent.getParcelableExtra(ACCOUNT_BALANCE_EXTRA) as ContractAccountBalance
        }
    }
}
