package com.memtrip.eosreach.app.issue.createaccount

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputFilter
import android.view.WindowManager
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.SkuDetails
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity

import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.welcome.EntryActivity
import com.memtrip.eosreach.billing.Billing
import com.memtrip.eosreach.billing.BillingError
import com.memtrip.eosreach.billing.BillingRequest
import com.memtrip.eosreach.billing.BillingResponse
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.inputfilter.AccountNameInputFilter
import com.memtrip.eosreach.uikit.invisible
import com.memtrip.eosreach.uikit.visible
import io.reactivex.Observable
import kotlinx.android.synthetic.main.issue_create_account_activity.*
import javax.inject.Inject

abstract class CreateAccountActivity
    : MviActivity<CreateAccountIntent, CreateAccountRenderAction, CreateAccountViewState, CreateAccountViewLayout>(), CreateAccountViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: CreateAccountViewRenderer

    private lateinit var billing: Billing

    private lateinit var billingRequest: BillingRequest

    private lateinit var skuDetails: SkuDetails

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

        billing = Billing(this, { response ->
            if (response.success) {
                model().publish(CreateAccountIntent.CreateAccount(
                    response.purchase!!.purchaseToken,
                    issue_create_account_name_input.text.toString()
                ))
            } else {
                showSkuError(response.error!!)
            }
        })

        billingRequest = BillingRequest(
            getString(R.string.app_default_create_account_sku_id),
            billing.billingClient)

        issue_create_account_create_button.setOnClickListener { launchBillingFlow() }

        issue_create_account_name_input.setOnEditorActionListener { _, _, _ ->
            launchBillingFlow()
            true }
    }

    private fun launchBillingFlow() {
        showCreateAccountProgress()
        billing.launchBillingFlow(skuDetails.sku, this)
    }

    override fun intents(): Observable<CreateAccountIntent> = Observable.merge(
        Observable.just(CreateAccountIntent.Init(billingRequest)),
        RxView.clicks(issue_create_account_import_key_done_button).map {
            CreateAccountIntent.Done(
                issue_create_account_import_key_label.text.toString())
        },
        issue_create_account_sku_error.retryClick().map {
            CreateAccountIntent.SetupGooglePlayBilling(billingRequest)
        }
    )

    override fun layout(): CreateAccountViewLayout = this

    override fun model(): CreateAccountViewModel = getViewModel(viewModelFactory)

    override fun render(): CreateAccountViewRenderer = render

    override fun showSkuProgress() {
        issue_create_account_sku_error.gone()
        issue_create_account_sku_progress.visible()
    }

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
    }

    override fun showCreateAccountProgress() {
        issue_create_account_progress.visible()
        issue_create_account_create_button.invisible()
    }

    override fun showCreateAccountError(error: String) {
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
        issue_create_account_import_key_error.gone()
        issue_create_account_import_key_progress.visible()
    }

    override fun showImportKeyError(error: String) {
        issue_create_account_import_key_progress.gone()
        issue_create_account_import_key_error.visible()
        issue_create_account_import_key_error.populate(
            getString(R.string.issue_create_account_import_key_error_title),
            error
        )
    }

    override fun showAccountCreated(privateKey: String) {
        issue_create_account_form_group.gone()
        issue_create_account_import_key_group.visible()
        issue_create_account_import_key_label.text = privateKey
    }

    override fun navigateToAccountList() {
        startActivity(EntryActivity.entryIntent(this))
        finish()
    }

    abstract override fun inject()
}