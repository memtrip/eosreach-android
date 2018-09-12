package com.memtrip.eosreach.app.account.vote.cast.producers

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.vote.cast.CastVoteActivity
import com.memtrip.eosreach.app.blockproducerlist.BlockProducerListActivity
import com.memtrip.eosreach.app.transaction.log.TransactionLogActivity
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.inputfilter.AccountNameInputFilter
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

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<CastProducersVoteIntent> = Observable.merge(
        Observable.just(CastProducersVoteIntent.Init),
        RxView.clicks(cast_producers_vote_button).map {
            CastProducersVoteIntent.Vote(
                eosAccount.accountName,
                getFormData()
            )
        },
        RxView.clicks(cast_producers_vote_blockproducer_form_add).map {
            CastProducersVoteIntent.AddProducerField(getNextPositionInForm(), getTotalInForm())
        }
    )

    private fun getNextPositionInForm(): Int {
        return (cast_producers_vote_blockproducer_form_container.getChildAt(
            cast_producers_vote_blockproducer_form_container.childCount - 1
        ).tag as Int) + 1
    }

    private fun getTotalInForm(): Int = cast_producers_vote_blockproducer_form_container.childCount

    private fun getFormData(): List<String> {
        return with (ArrayList<String>()) {
            for (i in 0 until cast_producers_vote_blockproducer_form_container.childCount) {
                val formItem: ViewGroup = cast_producers_vote_blockproducer_form_container.getChildAt(i) as ViewGroup
                val editText = formItem.getChildAt(0) as EditText
                add(editText.editableText.toString())
            }
            this
        }
    }

    override fun layout(): CastProducersVoteViewLayout = this

    override fun model(): CastProducersVoteViewModel = getViewModel(viewModelFactory)

    override fun render(): CastProducersVoteViewRenderer = render

    override fun addProducerField(position: Int) {
        model().publish(CastProducersVoteIntent.Idle)
        cast_producers_vote_blockproducer_form_container.addView(
            with (LayoutInflater.from(context!!).inflate(
                R.layout.account_cast_producers_vote_item_layout,
                null,
                false)) {

                val producerEditText: EditText = ((this as ViewGroup).getChildAt(0) as EditText)
                producerEditText.filters = arrayOf(
                    AccountNameInputFilter(),
                    InputFilter.LengthFilter(12)
                )

                val removeButton: Button = (this.getChildAt(1) as Button)
                if (position == 0)  {
                    removeButton.gone()
                } else {
                    removeButton.setOnClickListener {
                        model().publish(CastProducersVoteIntent.RemoveProducerField(
                            (it.parent as View).tag as Int))
                    }
                }
                tag = position
                this
            }
        )
    }

    override fun removeProducerField(position: Int) {
        for (i in 0 until cast_producers_vote_blockproducer_form_container.childCount) {
            val formItem: ViewGroup = cast_producers_vote_blockproducer_form_container.getChildAt(i) as ViewGroup
            if (formItem.tag == position) {
                cast_producers_vote_blockproducer_form_container.removeViewAt(i)
                break
            }
        }
    }

    override fun showProgress() {
        cast_producers_vote_progress.visible()
        cast_producers_vote_button.invisible()
    }

    override fun showError(message: String, log: String) {
        cast_producers_vote_progress.gone()
        cast_producers_vote_button.visible()

        AlertDialog.Builder(context!!)
            .setMessage(message)
            .setPositiveButton(R.string.transaction_view_log_position_button) { _, _ ->
                model().publish(CastProducersVoteIntent.ViewLog(log))
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
