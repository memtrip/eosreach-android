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
package com.memtrip.eosreach.app.issue.createaccount

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.view.WindowManager
import com.android.billingclient.api.SkuDetails
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity

import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.settings.SettingsActivity.Companion.settingsIntent
import com.memtrip.eosreach.app.welcome.EntryActivity
import com.memtrip.eosreach.billing.Billing
import com.memtrip.eosreach.billing.BillingConnectionResponse
import com.memtrip.eosreach.billing.BillingError

import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.inputfilter.AccountNameInputFilter
import com.memtrip.eosreach.uikit.invisible
import com.memtrip.eosreach.uikit.visible
import io.reactivex.Observable
import kotlinx.android.synthetic.main.issue_create_account_activity.*
import kotlinx.android.synthetic.main.uikit_error_composite_view.view.*
import javax.inject.Inject

abstract class CreateAccountActivity
    : MviActivity<CreateAccountIntent, CreateAccountRenderAction, CreateAccountViewState, CreateAccountViewLayout>(), CreateAccountViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: CreateAccountViewRenderer

    @Inject
    lateinit var billing: Billing

    private lateinit var skuDetails: SkuDetails

    private val handler = Handler()

    /**
     * After the account has been created, pressing back or attempting to
     * exit the screen will first trigger `Done`.
     */
    private var triggerAccountCreatedOnExit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.issue_create_account_activity)
        setSupportActionBar(issue_create_account_toolbars)
        supportActionBar!!.title = getString(R.string.issue_create_account_title)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE)

        issue_create_account_name_input.filters = arrayOf(
            AccountNameInputFilter(),
            InputFilter.LengthFilter(resources.getInteger(R.integer.app_account_name_length)))

        issue_create_account_limbo_settings_button.setOnClickListener {
            startActivity(settingsIntent(this))
        }

        issue_create_account_import_key_settings_button.setOnClickListener {
            startActivity(settingsIntent(this))
        }
    }

    override fun onDestroy() {
        billing.endConnection()
        super.onDestroy()
    }

    override fun onHomeUp() {
        if (triggerAccountCreatedOnExit) {
            model().publish(CreateAccountIntent.Done)
        } else {
            super.onHomeUp()
        }
    }

    override fun onBackPressed() {
        if (triggerAccountCreatedOnExit) {
            model().publish(CreateAccountIntent.Done)
        } else {
            super.onBackPressed()
        }
    }

    private fun handleBillingConnection(response: BillingConnectionResponse): Unit = when (response) {
        is BillingConnectionResponse.Success -> {
            model().publish(CreateAccountIntent.BillingSetupSuccess(response.skuDetails))
        }
        BillingConnectionResponse.SkuNotFound -> {
            model().publish(CreateAccountIntent.BillingSetupFailed(BillingError.SkuNotFound))
        }
        BillingConnectionResponse.SkuBillingUnavailable -> {
            model().publish(CreateAccountIntent.BillingSetupFailed(BillingError.SkuBillingUnavailable))
        }
        BillingConnectionResponse.SkuNotAvailable -> {
            model().publish(CreateAccountIntent.BillingSetupFailed(BillingError.SkuNotAvailable))
        }
        BillingConnectionResponse.SkuRequestFailed -> {
            model().publish(CreateAccountIntent.BillingSetupFailed(BillingError.SkuRequestFailed))
        }
        BillingConnectionResponse.BillingSetupFailed -> {
            model().publish(CreateAccountIntent.BillingSetupFailed(BillingError.BillingSetupFailed))
        }
    }

    override fun startBillingConnection() {
        issue_create_account_sku_error.gone()
        issue_create_account_sku_progress.visible()

        billing.startConnection({ response ->
            handler.post(StartConnectionRunnable(model(), CreateAccountIntent.BillingFlowComplete(
                issue_create_account_name_input.text.toString(),
                response)))
        }, { response ->
            handler.post(BillingConnectionRunnable(model(), response))
        })
    }

    private fun launchBillingFlow() {
        showCreateAccountProgress()
        billing.launchBillingFlow(skuDetails.sku, this)
    }

    override fun intents(): Observable<CreateAccountIntent> = Observable.mergeArray(
        Observable.just(CreateAccountIntent.Init),
        issue_create_account_sku_error.retryClick().map {
            CreateAccountIntent.StartBillingConnection
        },
        issue_create_account_limbo_error.retryClick().map {
            CreateAccountIntent.RetryLimbo
        },
        Observable.merge(
            RxView.clicks(issue_create_account_import_key_done_button),
            issue_create_account_import_key_error.retryClick()
        ).map {
            CreateAccountIntent.Done
        },
        Observable.merge(
            RxView.clicks(issue_create_account_create_button),
            RxTextView.editorActions(issue_create_account_name_input)
        ).map {
            hideKeyboard()
            CreateAccountIntent.BuyAccount(issue_create_account_name_input.editableText.toString())
        }
    )

    override fun layout(): CreateAccountViewLayout = this

    override fun model(): CreateAccountViewModel = getViewModel(viewModelFactory)

    override fun render(): CreateAccountViewRenderer = render

    override fun showSkuSuccess(skuDetails: SkuDetails) {
        this.skuDetails = skuDetails
        issue_create_account_create_button.text = getString(
            R.string.issue_create_account_create_button, skuDetails.price)
        issue_create_account_create_button.visible()
        issue_create_account_form_group.visible()
        issue_create_account_sku_progress.gone()
    }

    override fun showSkuError(message: String) {
        issue_create_account_sku_progress.gone()
        issue_create_account_sku_error.visible()
        issue_create_account_sku_error.populate(
            getString(R.string.app_dialog_error_title),
            message
        )
        issue_create_account_sku_error.view_error_composite_retry.setOnClickListener {
            startBillingConnection()
        }
    }

    override fun showAccountNameValidationPassed() {
        launchBillingFlow()
    }

    override fun showCreateAccountLimbo() {
        issue_create_account_form_group.gone()
        issue_create_account_progress.gone()
        issue_create_account_limbo_progress.gone()
        issue_create_account_limbo_error.visible()
        issue_create_account_limbo_settings_button.visible()
        issue_create_account_limbo_error.populate(
            getString(R.string.issue_create_account_limbo_title),
            getString(R.string.issue_create_account_limbo_body))
    }

    override fun showCreateAccountLimboProgress() {
        issue_create_account_limbo_progress.visible()
        issue_create_account_limbo_error.gone()
        issue_create_account_limbo_settings_button.gone()
    }

    override fun showCreateAccountProgress() {
        issue_create_account_progress.visible()
        issue_create_account_create_button.invisible()
        issue_create_account_create_button.text = getString(R.string.issue_create_account_create_purchased_button)
    }

    override fun showCreateAccountError(error: String) {

        model().publish(CreateAccountIntent.Idle)

        issue_create_account_limbo_progress.gone()
        issue_create_account_form_group.visible()
        issue_create_account_progress.gone()
        issue_create_account_create_button.visible()

        AlertDialog.Builder(this)
            .setTitle(R.string.app_dialog_error_title)
            .setMessage(error)
            .setPositiveButton(R.string.app_dialog_positive_button, null)
            .create()
            .show()
    }

    override fun showImportKeyProgress() {
        issue_create_account_import_key_settings_button.gone()
        issue_create_account_import_key_error.gone()
        issue_create_account_import_key_group.gone()
        issue_create_account_import_key_done_button.gone()
        issue_create_account_import_key_progress.visible()
    }

    override fun showImportKeyError(error: String) {
        triggerAccountCreatedOnExit = false
        issue_create_account_form_group.gone()
        issue_create_account_progress.gone()
        issue_create_account_import_key_progress.gone()
        issue_create_account_import_key_error.visible()
        issue_create_account_import_key_settings_button.visible()
        issue_create_account_import_key_error.populate(
            getString(R.string.issue_create_account_import_key_error_title),
            error
        )
    }

    override fun showAccountCreated(purchaseToken: String, privateKey: String) {
        billing.consumeItem(purchaseToken) {
            triggerAccountCreatedOnExit = true
            issue_create_account_limbo_progress.gone()
            issue_create_account_form_group.gone()
            issue_create_account_progress.gone()
            issue_create_account_import_key_group.visible()
            issue_create_account_import_key_done_button.visible()
            issue_create_account_import_key_label.text = privateKey
        }
    }

    override fun navigateToAccountList() {
        startActivity(EntryActivity.entryIntent(this))
        finish()
    }

    abstract override fun inject()
}