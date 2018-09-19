package com.memtrip.eosreach.app.issue.createaccount

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.view.WindowManager
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.MviActivity

import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.welcome.EntryActivity
import com.memtrip.eosreach.billing.Billing
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
    : MviActivity<CreateAccountIntent, CreateAccountRenderAction, CreateAccountViewState, CreateAccountViewLayout>(
), CreateAccountViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: CreateAccountViewRenderer

    private lateinit var billing: Billing
    private lateinit var skuDetails: SkuDetails

    private val handler = Handler()

    private var accountCreated: Boolean = false

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

        issue_create_account_sku_error.setOnClickListener {
            startBillingConnection()
        }

        billing = Billing(this, { response ->
            if (response.success) {
                handler.post {
                    model().publish(CreateAccountIntent.CreateAccount(
                        response.purchaseToken!!,
                        issue_create_account_name_input.text.toString()
                    ))
                }
            } else {
                handler.post {
                    showCreateAccountError(response.error!!)
                }
            }
        })

        startBillingConnection()
    }

    override fun onDestroy() {
        billing.billingClient.endConnection()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (accountCreated) {
            model().publish(CreateAccountIntent.Done(issue_create_account_import_key_label.text.toString()))
        } else {
            super.onBackPressed()
        }
    }

    private fun startBillingConnection() {
        billing.billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(responseCode: Int) {
                if (responseCode == BillingClient.BillingResponse.OK) {
                    billing.billingClient.querySkuDetailsAsync(with (SkuDetailsParams.newBuilder()) {
                        setSkusList(with (ArrayList<String>()) {
                            add(getString(R.string.app_default_create_account_sku_id))
                            this
                        })
                        setType(BillingClient.SkuType.INAPP)
                        this
                    }.build()) { skuResponseCode, skuDetailsList ->
                        if (skuResponseCode == BillingClient.BillingResponse.OK) {
                            if (skuDetailsList.isNotEmpty()) {
                                model().publish(CreateAccountIntent.BillingSetupSuccess(skuDetailsList[0]))
                            } else {
                                model().publish(CreateAccountIntent.BillingSetupFailed(BillingError.SkuNotFound))
                            }
                        } else if (skuResponseCode == BillingClient.BillingResponse.BILLING_UNAVAILABLE) {
                            model().publish(CreateAccountIntent.BillingSetupFailed(BillingError.SkuBillingUnavailable))
                        } else if (skuResponseCode == BillingClient.BillingResponse.ITEM_UNAVAILABLE) {
                            model().publish(CreateAccountIntent.BillingSetupFailed(BillingError.SkuNotAvailable))
                        } else if (skuResponseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
                            model().publish(CreateAccountIntent.BillingSetupFailed(BillingError.SkuAlreadyOwned))
                        } else {
                            model().publish(CreateAccountIntent.BillingSetupFailed(BillingError.SkuRequestFailed))
                        }
                    }
                } else {
                    model().publish(CreateAccountIntent.BillingSetupFailed(BillingError.BillingSetupFailed))
                }
            }
            override fun onBillingServiceDisconnected() {
            }
        })
    }

    private fun launchBillingFlow() {
        showCreateAccountProgress()
        billing.launchBillingFlow(skuDetails.sku, this)
    }

    override fun intents(): Observable<CreateAccountIntent> = Observable.merge(
        Observable.just(CreateAccountIntent.Init),
        RxView.clicks(issue_create_account_import_key_done_button).map {
            CreateAccountIntent.Done(
                issue_create_account_import_key_label.text.toString())
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
        issue_create_account_sku_error.view_error_composite_retry.setOnClickListener {
            startBillingConnection()
        }
    }

    override fun showAccountNameValidationPassed() {
        launchBillingFlow()
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
        issue_create_account_import_key_group.gone()
        issue_create_account_import_key_done_button.gone()
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

    override fun showAccountCreated(purchaseToken: String, privateKey: String) {
        billing.billingClient.consumeAsync(purchaseToken) { _, _ ->
            accountCreated = true
            issue_create_account_form_group.gone()
            issue_create_account_progress.gone()
            issue_create_account_import_key_group.visible()
            issue_create_account_import_key_done_button.visible()
            issue_create_account_import_key_label.text = privateKey
            billing.billingClient.endConnection()
        }
    }

    override fun navigateToAccountList() {
        startActivity(EntryActivity.entryIntent(this))
        finish()
    }

    abstract override fun inject()
}