package com.memtrip.eosreach.app.account.vote

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.blockproducerlist.BlockProducerListActivity
import com.memtrip.eosreach.app.transaction.log.TransactionLogActivity
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.invisible
import com.memtrip.eosreach.uikit.visible
import dagger.android.AndroidInjection

import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.account_cast_vote_fragment.*
import kotlinx.android.synthetic.main.transfer_confirm_activity.*
import javax.inject.Inject

class CastVoteActivity
    : MviActivity<CastVoteIntent, CastVoteRenderAction, CastVoteViewState, CastVoteViewLayout>(), CastVoteViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: CastVoteViewRenderer

    lateinit var eosAccount: EosAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_cast_vote_fragment)
        eosAccount = fromIntent(intent!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == BlockProducerListActivity.RESULT_CODE) {
            val blockProducerBundle = BlockProducerListActivity.fromIntent(data!!)
            account_cast_vote_blockproducer_name_input.setText(blockProducerBundle.apiUrl, TextView.BufferType.EDITABLE)
        }
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun intents(): Observable<CastVoteIntent> {
        return RxView.clicks(account_cast_vote_button).map {
            CastVoteIntent.Vote(
                eosAccount.accountName,
                account_cast_vote_blockproducer_name_input.text.toString()
            )
        }
    }

    override fun layout(): CastVoteViewLayout = this

    override fun model(): CastVoteViewModel = getViewModel(viewModelFactory)

    override fun render(): CastVoteViewRenderer = render

    override fun showProgress() {
        account_cast_vote_progressbar.visible()
        account_cast_vote_button.invisible()
    }

    override fun showError(message: String, log: String) {
        account_cast_vote_progressbar.gone()
        account_cast_vote_button.visible()

        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(R.string.transaction_view_log_position_button) { _, _ ->
                model().publish(CastVoteIntent.ViewLog(log))
            }
            .setNegativeButton(R.string.transaction_view_log_negative_button, null)
            .create()
            .show()

        transfer_confirm_progress.gone()
        transfer_confirm_confirm_button.visible()
    }

    override fun onSuccess() {
        // TODO: refresh the parent account activity
    }

    override fun navigateToBlockProducerList() {
        model().publish(CastVoteIntent.Idle)
        startActivityForResult(BlockProducerListActivity.blockProducerList(this), BlockProducerListActivity.RESULT_CODE)
    }

    override fun viewLog(log: String) {
        model().publish(CastVoteIntent.Idle)
        startActivity(TransactionLogActivity.transactionLogIntent(log, this))
    }

    companion object {

        const val EOS_ACCOUNT_EXTRA = "EOS_ACCOUNT_EXTRA"

        fun castVoteIntent(eosAccount: EosAccount, context: Context): Intent {
            return with (Intent(context, CastVoteActivity::class.java)) {
                putExtra(EOS_ACCOUNT_EXTRA, eosAccount)
                this
            }
        }

        private fun fromIntent(intent: Intent): EosAccount {
            return intent.getParcelableExtra("eosAccount") as EosAccount
        }
    }
}
