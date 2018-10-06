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
package com.memtrip.eosreach.app.account.vote

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.account.EosAccountVote
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountFragmentPagerAdapter
import com.memtrip.eosreach.app.account.AccountParentRefresh
import com.memtrip.eosreach.app.account.AccountTheme
import com.memtrip.eosreach.app.account.resources.ResourcesFragment
import com.memtrip.eosreach.app.account.vote.cast.CastVoteActivity
import com.memtrip.eosreach.app.account.vote.cast.CastVoteActivity.Companion.castVoteIntent
import com.memtrip.eosreach.app.account.vote.cast.CastVoteFragmentPagerFragment
import com.memtrip.eosreach.app.transaction.log.TransactionLogActivity.Companion.transactionLogIntent
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.invisible
import com.memtrip.eosreach.uikit.visible
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_vote_fragment.*
import kotlinx.android.synthetic.main.account_vote_fragment.view.*
import javax.inject.Inject

abstract class VoteFragment
    : MviFragment<VoteIntent, VoteRenderAction, VoteViewState, VoteViewLayout>(), VoteViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: VoteViewRenderer

    private lateinit var adapter: VoteProducerAdapter
    private lateinit var eosAccount: EosAccount
    private lateinit var accountParentRefresh: AccountParentRefresh

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        accountParentRefresh = context as AccountParentRefresh
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.account_vote_fragment, container, false)

        eosAccount = eosAccountBundle(arguments!!)

        val adapterInteraction: PublishSubject<Interaction<String>> = PublishSubject.create()
        adapter = VoteProducerAdapter(context!!, adapterInteraction)
        view.vote_producer_vote_list_recyclerview.adapter = adapter

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CastVoteActivity.CAST_VOTE_REQUEST_CODE &&
            resultCode == CastVoteActivity.CAST_VOTE_RESULT_CODE) {
            accountParentRefresh.triggerRefresh(AccountFragmentPagerAdapter.Page.VOTE)
        }
    }

    override fun intents(): Observable<VoteIntent> = Observable.merge(
        Observable.just(VoteIntent.Init(eosAccountBundle(arguments!!).eosAcconuntVote)),
        RxView.clicks(vote_cast_vote_producer_button).map { VoteIntent.NavigateToCastProducerVote },
        RxView.clicks(vote_cast_vote_proxy_button).map { VoteIntent.NavigateToCastProxyVote },
        RxView.clicks(vote_no_vote_castvote_button).map { VoteIntent.VoteForUs(eosAccount) }
    )

    override fun layout(): VoteViewLayout = this

    override fun model(): VoteViewModel = getViewModel(viewModelFactory)

    override fun render(): VoteViewRenderer = render

    override fun populateProxyVote(proxyVoter: String) {
        vote_no_vote_castvote_button.gone()
        vote_proxy_group.visible()
        vote_proxy_voter.text = proxyVoter
    }

    override fun populateProducerVotes(eosAccountVote: EosAccountVote) {
        vote_no_vote_castvote_button.gone()
        vote_producer_vote_group.visible()
        adapter.clear()
        adapter.populate(eosAccountVote.producers)
    }

    override fun showNoVoteCast() {
        vote_no_vote_castvote_button.visible()
        vote_no_vote_group.visible()
    }

    override fun navigateToCastProducerVote() {
        model().publish(VoteIntent.Idle)
        startActivityForResult(castVoteIntent(
            eosAccount,
            CastVoteFragmentPagerFragment.Page.PRODUCER,
            context!!
        ), CastVoteActivity.CAST_VOTE_REQUEST_CODE)
    }

    override fun navigateToCastProxyVote() {
        model().publish(VoteIntent.Idle)
        startActivityForResult(castVoteIntent(
            eosAccount,
            CastVoteFragmentPagerFragment.Page.PROXY,
            context!!
        ), CastVoteActivity.CAST_VOTE_REQUEST_CODE)
    }

    override fun showVoteForUsProgress() {
        vote_no_vote_castvote_progressbar.visible()
        vote_no_vote_castvote_button.invisible()
    }

    override fun voteForUsSuccess() {
        accountParentRefresh.triggerRefresh(AccountFragmentPagerAdapter.Page.VOTE)
    }

    override fun voteForUsError(message: String, log: String) {
        vote_no_vote_castvote_progressbar.gone()
        vote_no_vote_castvote_button.visible()

        AlertDialog.Builder(context!!)
            .setMessage(message)
            .setPositiveButton(R.string.transaction_view_log_position_button) { _, _ ->
                model().publish(VoteIntent.Idle)
                startActivity(transactionLogIntent(log, context!!))
            }
            .setNegativeButton(R.string.transaction_view_log_negative_button, null)
            .create()
            .show()
    }

    companion object {

        private const val EOS_ACCOUNT = "EOS_ACCOUNT"

        fun newInstance(
            eosAccount: EosAccount,
            accountTheme: AccountTheme
        ): VoteFragment {
            return when (accountTheme) {
                AccountTheme.DEFAULT -> {
                    with (DefaultVoteFragment()) {
                        arguments = with (Bundle()) {
                            putParcelable(EOS_ACCOUNT, eosAccount)
                            this
                        }
                        this
                    }
                }
                AccountTheme.READ_ONLY -> {
                    with (ReadOnlyVoteFragment()) {
                        arguments = with (Bundle()) {
                            putParcelable(EOS_ACCOUNT, eosAccount)
                            this
                        }
                        this
                    }
                }
            }
        }

        private fun eosAccountBundle(bundle: Bundle): EosAccount =
            bundle.getParcelable(EOS_ACCOUNT)
    }
}