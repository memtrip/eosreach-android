package com.memtrip.eosreach.app.account.vote.cast.producers

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.blockproducerlist.BlockProducerListActivity
import com.memtrip.eosreach.app.transaction.log.TransactionLogActivity
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.invisible
import com.memtrip.eosreach.uikit.visible

import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.account_cast_producers_vote_fragment.*
import javax.inject.Inject

class CastProducersVoteFragment
    : MviFragment<CastProducersVoteIntent, CastProducersVoteRenderAction, CastProducersVoteViewState, CastProducersVoteViewLayout>(), CastProducersVoteViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: CastProducersVoteViewRenderer

    private lateinit var eosAccount: EosAccount

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.account_cast_producers_vote_fragment, container, false)
        eosAccount = fromBundle(arguments!!)
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == BlockProducerListActivity.RESULT_CODE) {
            val blockProducerBundle = BlockProducerListActivity.fromIntent(data!!)
            cast_producer_vote_blockproducer_name_input.setText(blockProducerBundle.apiUrl, TextView.BufferType.EDITABLE)
        }
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<CastProducersVoteIntent> {
        return RxView.clicks(cast_producer_vote_button).map {
            CastProducersVoteIntent.Vote(
                eosAccount.accountName,
                cast_producer_vote_blockproducer_name_input.text.toString()
            )
        }
    }

    override fun layout(): CastProducersVoteViewLayout = this

    override fun model(): CastProducersVoteViewModel = getViewModel(viewModelFactory)

    override fun render(): CastProducersVoteViewRenderer = render

    override fun showProgress() {
        cast_producer_vote_progress.visible()
        cast_producer_vote_button.invisible()
    }

    override fun showError(message: String, log: String) {
        cast_producer_vote_progress.gone()
        cast_producer_vote_button.visible()

        AlertDialog.Builder(context!!)
            .setMessage(message)
            .setPositiveButton(R.string.transaction_view_log_position_button) { _, _ ->
                model().publish(CastProducersVoteIntent.ViewLog(log))
            }
            .setNegativeButton(R.string.transaction_view_log_negative_button, null)
            .create()
            .show()

        cast_producer_vote_progress.gone()
        cast_producer_vote_button.visible()
    }

    override fun onSuccess() {
        // TODO: refresh the parent account activity
    }

    override fun navigateToBlockProducerList() {
        model().publish(CastProducersVoteIntent.Idle)
        startActivityForResult(BlockProducerListActivity.blockProducerList(context!!), BlockProducerListActivity.RESULT_CODE)
    }

    override fun viewLog(log: String) {
        model().publish(CastProducersVoteIntent.Idle)
        startActivity(TransactionLogActivity.transactionLogIntent(log, context!!))
    }

    companion object {

        private const val EOS_ACCOUNT_EXTRA = "EOS_ACCOUNT_EXTRA"

        fun newInstance(eosAccount: EosAccount): CastProducersVoteFragment = with (CastProducersVoteFragment()) {
            arguments = toBundle(eosAccount)
            this
        }

        private fun toBundle(eosAccount: EosAccount): Bundle = with (Bundle()) {
            putParcelable(EOS_ACCOUNT_EXTRA, eosAccount)
            this
        }

        private fun fromBundle(bundle: Bundle): EosAccount = bundle.getParcelable(EOS_ACCOUNT_EXTRA)
    }
}
