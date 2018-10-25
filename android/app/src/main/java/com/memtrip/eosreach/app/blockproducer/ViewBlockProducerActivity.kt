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
import com.memtrip.eosreach.app.account.AccountTheme
import com.memtrip.eosreach.app.account.ReadonlyAccountActivity.Companion.accountReadOnlyIntent
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible

import io.reactivex.Observable
import kotlinx.android.synthetic.main.block_producer_view_activity.*
import javax.inject.Inject

abstract class ViewBlockProducerActivity
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
        supportActionBar!!.title = ""
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        block_producer_view_owner_account_button.setOnClickListener {
            model().publish(ViewBlockProducerIntent.Idle)
            startActivity(accountReadOnlyIntent(AccountBundle(blockProducerDetails.owner), this))
        }

        block_producer_view_email_label.setOnClickListener {
            model().publish(ViewBlockProducerIntent.Idle)
            triggerEmailIntent()
        }
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
        },
        block_producer_view_error.retryClick().map {
            ViewBlockProducerIntent.InitWithName(blockProducerName(intent))
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
        block_producer_view_error.gone()
        block_producer_view_group.gone()
        block_producer_view_owner_account_button.gone()
    }

    override fun showError() {
        block_producer_view_progress.gone()
        block_producer_view_error.visible()
        block_producer_view_error.populate(
            getString(R.string.app_dialog_error_title),
            getString(R.string.block_producer_view_error_body))
    }

    override fun showEmpty() {
        block_producer_view_progress.gone()
        block_producer_view_empty_label.visible()
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
        block_producer_view_owner_account_button.visible()

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
            accountTheme: AccountTheme,
            blockProducerDetails: BlockProducerDetails,
            context: Context
        ): Intent {
            return when (accountTheme) {
                AccountTheme.DEFAULT -> {
                    with(Intent(context, DefaultViewBlockProducerActivity::class.java)) {
                        putExtra(BLOCK_PRODUCER_DETAILS, blockProducerDetails)
                        putExtra(BLOCK_PRODUCER_DISPLAY_ACTION, ViewBlockProducerDisplayAction.DETAILS)
                        this
                    }
                }
                AccountTheme.READ_ONLY -> {
                    with(Intent(context, ReadOnlyViewBlockProducerActivity::class.java)) {
                        putExtra(BLOCK_PRODUCER_DETAILS, blockProducerDetails)
                        putExtra(BLOCK_PRODUCER_DISPLAY_ACTION, ViewBlockProducerDisplayAction.DETAILS)
                        this
                    }
                }
            }
        }

        fun viewBlockProducerIntentWithName(
            accountTheme: AccountTheme,
            blockProducerName: String,
            context: Context
        ): Intent {
            return when (accountTheme) {
                AccountTheme.DEFAULT -> {
                    with(Intent(context, DefaultViewBlockProducerActivity::class.java)) {
                        putExtra(BLOCK_PRODUCER_NAME, blockProducerName)
                        putExtra(BLOCK_PRODUCER_DISPLAY_ACTION, ViewBlockProducerDisplayAction.LOAD)
                        this
                    }
                }
                AccountTheme.READ_ONLY -> {
                    with(Intent(context, ReadOnlyViewBlockProducerActivity::class.java)) {
                        putExtra(BLOCK_PRODUCER_NAME, blockProducerName)
                        putExtra(BLOCK_PRODUCER_DISPLAY_ACTION, ViewBlockProducerDisplayAction.LOAD)
                        this
                    }
                }
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