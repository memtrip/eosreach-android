package com.memtrip.eosreach.app.blockproducer

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.blockproducer.BlockProducerDetails
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountBundle
import com.memtrip.eosreach.app.account.ReadonlyAccountActivity
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.block_producer_view_activity.*
import javax.inject.Inject

class ViewBlockProducerActivity
    : MviActivity<ViewBlockProducerIntent, ViewBlockProducerRenderAction, ViewBlockProducerViewState, ViewBlockProducerViewLayout>(), ViewBlockProducerViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: ViewBlockProducerViewRenderer

    private lateinit var blockProducerDetails: BlockProducerDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.block_producer_view_activity)
        setSupportActionBar(block_producer_view_toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        block_producer_view_owner_account_button.setOnClickListener {
            model().publish(ViewBlockProducerIntent.Idle)
            startActivity(ReadonlyAccountActivity.accountReadOnlyIntent(AccountBundle(blockProducerDetails.owner), this))
        }

        block_producer_view_email_label.setOnClickListener {
            model().publish(ViewBlockProducerIntent.Idle)
            triggerEmailIntent()
        }
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<ViewBlockProducerIntent> = Observable.mergeArray(
        startingIntent(displayAction(intent)),
        RxView.clicks(block_producer_view_code_of_conduct_button).map {
            ViewBlockProducerIntent.NavigateToUrl(blockProducerDetails.codeOfConductUrl)
        },
        RxView.clicks(block_producer_view_ownership_disclosure_button).map {
            ViewBlockProducerIntent.NavigateToUrl(blockProducerDetails.ownershipDisclosureUrl)
        },
        RxView.clicks(block_producer_view_website_label).map {
            ViewBlockProducerIntent.NavigateToUrl(blockProducerDetails.website)
        }
    )

    private fun startingIntent(displayAction: ViewBlockProducerDisplayAction): Observable<ViewBlockProducerIntent> {
        return when (displayAction) {
            ViewBlockProducerDisplayAction.DETAILS -> {
                Observable.just(ViewBlockProducerIntent.InitWithDetails(blockProducerDetails(intent)))
            }
            ViewBlockProducerDisplayAction.LOAD -> {
                Observable.just(ViewBlockProducerIntent.InitWithName(blockProducerName(intent)))
            }
        }
    }

    private fun triggerEmailIntent() {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
            "mailto", blockProducerDetails.email, null))
        startActivity(Intent.createChooser(emailIntent, getString(R.string.block_producer_email, blockProducerDetails.candidateName)))
    }

    override fun layout(): ViewBlockProducerViewLayout = this

    override fun model(): ViewBlockProducerViewModel = getViewModel(viewModelFactory)

    override fun render(): ViewBlockProducerViewRenderer = render

    override fun showProgress() {
        block_producer_view_progress.visible()
        block_producer_view_error_view.gone()
        block_producer_view_group.gone()
    }

    override fun showError() {
        block_producer_view_progress.gone()
        block_producer_view_error_view.visible()
    }

    override fun showTitle(blockProducerName: String) {
        supportActionBar!!.title = blockProducerName
    }

    override fun invalidUrl(url: String) {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.block_producer_view_invalid_url, url))
            .setPositiveButton(R.string.app_dialog_positive_button, null)
            .create()
            .show()
    }

    override fun navigateToUrl(url: String) {
        model().publish(ViewBlockProducerIntent.Idle)
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    override fun populate(blockProducerDetails: BlockProducerDetails) {

        block_producer_view_progress.gone()
        block_producer_view_group.visible()

        this.blockProducerDetails = blockProducerDetails

        supportActionBar!!.title = blockProducerDetails.owner
        block_producer_view_candidate_name_label.text = blockProducerDetails.candidateName
        block_producer_view_website_label.text = blockProducerDetails.website
        block_producer_view_email_label.text = blockProducerDetails.email

        blockProducerDetails.logo256?.let { url ->
            block_producer_view_icon.setImageURI(url)
        }
    }

    companion object {

        private const val BLOCK_PRODUCER_DETAILS = "BLOCK_PRODUCER_DETAILS"
        private const val BLOCK_PRODUCER_NAME = "BLOCK_PRODUCER_NAME"
        private const val BLOCK_PRODUCER_DISPLAY_ACTION = "BLOCK_PRODUCER_DISPLAY_ACTION"

        fun viewBlockProducerIntentWithDetails(
            blockProducerDetails: BlockProducerDetails,
            context: Context
        ): Intent {
            return with (Intent(context, ViewBlockProducerActivity::class.java)) {
                putExtra(BLOCK_PRODUCER_DETAILS, blockProducerDetails)
                putExtra(BLOCK_PRODUCER_DISPLAY_ACTION, ViewBlockProducerDisplayAction.DETAILS)
                this
            }
        }

        fun viewBlockProducerIntentWithName(
            blockProducerName: String,
            context: Context
        ): Intent {
            return with (Intent(context, ViewBlockProducerActivity::class.java)) {
                putExtra(BLOCK_PRODUCER_NAME, blockProducerName)
                putExtra(BLOCK_PRODUCER_DISPLAY_ACTION, ViewBlockProducerDisplayAction.LOAD)
                this
            }
        }

        private fun blockProducerDetails(intent: Intent): BlockProducerDetails {
            return intent.getParcelableExtra(BLOCK_PRODUCER_DETAILS)
        }

        private fun blockProducerName(intent: Intent): String = intent.getStringExtra(BLOCK_PRODUCER_NAME)

        private fun displayAction(intent: Intent): ViewBlockProducerDisplayAction {
            return intent.getSerializableExtra(BLOCK_PRODUCER_DISPLAY_ACTION) as ViewBlockProducerDisplayAction
        }
    }
}