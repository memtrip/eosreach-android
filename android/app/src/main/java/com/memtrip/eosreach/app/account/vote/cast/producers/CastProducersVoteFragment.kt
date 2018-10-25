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
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.vote.cast.CastVoteActivity
import com.memtrip.eosreach.app.blockproducer.BlockProducerListActivity
import com.memtrip.eosreach.app.blockproducer.BlockProducerListActivity.Companion.blockProducerListIntent
import com.memtrip.eosreach.app.transaction.log.TransactionLogActivity.Companion.transactionLogIntent
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.inputfilter.AccountNameInputFilter
import com.memtrip.eosreach.uikit.invisible
import com.memtrip.eosreach.uikit.visible
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.account_cast_producers_vote_fragment.*
import kotlinx.android.synthetic.main.account_cast_producers_vote_fragment.view.*
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
        eosAccount = eosAccountExtra(arguments!!)

        view.cast_producers_vote_blockproducer_form_block_producer_list.setOnClickListener {
            model().publish(CastProducersVoteIntent.Idle)
            startActivityForResult(blockProducerListIntent(context!!), 0)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == BlockProducerListActivity.RESULT_CODE) {
            val blockProducerBundle = BlockProducerListActivity.blockProducerDetailsFromIntent(data!!)
            model().publish(CastProducersVoteIntent.InsertProducerField(
                getNextPositionInForm(),
                getTotalInForm(),
                blockProducerBundle.owner
            ))
        }
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<CastProducersVoteIntent> = Observable.merge(
        Observable.just(CastProducersVoteIntent.Init(eosAccount.eosAcconuntVote)),
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
        return if (cast_producers_vote_blockproducer_form_container.childCount == 0) {
            0
        } else {
            (cast_producers_vote_blockproducer_form_container.getChildAt(
                cast_producers_vote_blockproducer_form_container.childCount - 1
            ).tag as Int) + 1
        }
    }

    private fun getTotalInForm(): Int = cast_producers_vote_blockproducer_form_container.childCount

    private fun getFormData(): List<String> {
        return with(ArrayList<String>()) {
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

    override fun populateWidthExistingProducerList(producers: List<String>) {
        model().publish(CastProducersVoteIntent.Idle)
        producers.forEachIndexed { index, producer ->
            insertProducerField(index, producer)
        }
    }

    override fun addProducerRow(position: Int) {
        model().publish(CastProducersVoteIntent.Idle)
        insertProducerField(position)
    }

    override fun insertProducerField(position: Int, value: String) {
        model().publish(CastProducersVoteIntent.Idle)
        cast_producers_vote_blockproducer_form_container.addView(
            with(LayoutInflater.from(context!!).inflate(
                R.layout.account_cast_producers_vote_item_layout,
                null,
                false)) {

                val producerEditText: EditText = ((this as ViewGroup).getChildAt(0) as EditText)
                producerEditText.filters = arrayOf(
                    AccountNameInputFilter(),
                    InputFilter.LengthFilter(12)
                )
                producerEditText.setText(value)

                val removeButton: Button = (this.getChildAt(1) as Button)
                removeButton.setOnClickListener {
                    model().publish(CastProducersVoteIntent.RemoveProducerField(
                        (it.parent as View).tag as Int))
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
        startActivity(transactionLogIntent(log, context!!))
    }

    companion object {

        private const val EOS_ACCOUNT_EXTRA = "EOS_ACCOUNT_EXTRA"

        fun newInstance(eosAccount: EosAccount): CastProducersVoteFragment = with(CastProducersVoteFragment()) {
            arguments = toBundle(eosAccount)
            this
        }

        private fun toBundle(eosAccount: EosAccount): Bundle = with(Bundle()) {
            putParcelable(EOS_ACCOUNT_EXTRA, eosAccount)
            this
        }

        private fun eosAccountExtra(bundle: Bundle): EosAccount = bundle.getParcelable(EOS_ACCOUNT_EXTRA)
    }
}
