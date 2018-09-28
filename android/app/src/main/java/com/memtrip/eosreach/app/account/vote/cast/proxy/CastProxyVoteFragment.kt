package com.memtrip.eosreach.app.account.vote.cast.proxy

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount

import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ParentActivity
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.vote.cast.CastVoteActivity

import com.memtrip.eosreach.app.transaction.log.TransactionLogActivity

import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.inputfilter.AccountNameInputFilter
import com.memtrip.eosreach.uikit.invisible
import com.memtrip.eosreach.uikit.visible

import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.account_cast_proxy_vote_fragment.*
import kotlinx.android.synthetic.main.account_cast_proxy_vote_fragment.view.*
import javax.inject.Inject

class CastProxyVoteFragment
    : MviFragment<CastProxyVoteIntent, CastProxyVoteRenderAction, CastProxyVoteViewState, CastProxyVoteViewLayout>(), CastProxyVoteViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: CastProxyVoteViewRenderer

    private lateinit var eosAccount: EosAccount

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.account_cast_proxy_vote_fragment, container, false)
        eosAccount = fromBundle(arguments!!)
        view.cast_proxy_vote_name_input.filters = arrayOf(
            AccountNameInputFilter(),
            InputFilter.LengthFilter(context!!.resources.getInteger(R.integer.app_account_name_length))
        )
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<CastProxyVoteIntent> {
        return Observable.merge(
            RxView.clicks(cast_proxy_vote_button),
            RxTextView.editorActions(cast_proxy_vote_name_input)
        ).map {
            (activity!! as ParentActivity).hideKeyboard()
            CastProxyVoteIntent.Vote(
                eosAccount.accountName,
                cast_proxy_vote_name_input.editableText.toString()
            )
        }
    }

    override fun layout(): CastProxyVoteViewLayout = this

    override fun model(): CastProxyVoteViewModel = getViewModel(viewModelFactory)

    override fun render(): CastProxyVoteViewRenderer = render

    override fun showProgress() {
        cast_proxy_vote_progress.visible()
        cast_proxy_vote_button.invisible()
    }

    override fun showError(message: String, log: String) {
        cast_proxy_vote_progress.gone()
        cast_proxy_vote_button.visible()

        AlertDialog.Builder(context!!)
            .setMessage(message)
            .setPositiveButton(R.string.transaction_view_log_position_button) { _, _ ->
                model().publish(CastProxyVoteIntent.ViewLog(log))
            }
            .setNegativeButton(R.string.transaction_view_log_negative_button, null)
            .create()
            .show()
    }

    override fun onSuccess() {
        activity!!.setResult(CastVoteActivity.CAST_VOTE_RESULT_CODE, null)
        activity!!.finish()
    }

    override fun viewLog(log: String) {
        model().publish(CastProxyVoteIntent.Idle)
        startActivity(TransactionLogActivity.transactionLogIntent(log, context!!))
    }

    override fun populateProxyVoteInput(value: String) {
        cast_proxy_vote_name_input.setText(value)
    }

    companion object {

        private const val EOS_ACCOUNT_EXTRA = "EOS_ACCOUNT_EXTRA"

        fun newInstance(eosAccount: EosAccount): CastProxyVoteFragment = with (CastProxyVoteFragment()) {
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
