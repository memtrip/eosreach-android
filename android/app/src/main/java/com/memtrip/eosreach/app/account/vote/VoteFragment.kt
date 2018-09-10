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
import com.memtrip.eosreach.app.account.AccountPagerFragment
import com.memtrip.eosreach.app.account.AccountParentRefresh
import com.memtrip.eosreach.app.account.vote.cast.CastVoteActivity
import com.memtrip.eosreach.app.account.vote.cast.CastVoteActivity.Companion.castVoteIntent
import com.memtrip.eosreach.app.account.vote.cast.proxy.CastProxyVoteIntent
import com.memtrip.eosreach.app.transaction.log.TransactionLogActivity
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.invisible
import com.memtrip.eosreach.uikit.visible
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.vote_fragment.*
import kotlinx.android.synthetic.main.vote_fragment.view.*
import javax.inject.Inject

class VoteFragment
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
        val view = inflater.inflate(R.layout.vote_fragment, container, false)

        eosAccount = fromBundle(arguments!!)

        val adapterInteraction: PublishSubject<Interaction<String>> = PublishSubject.create()
        adapter = VoteProducerAdapter(context!!, adapterInteraction)
        view.vote_producer_vote_list_recyclerview.adapter = adapter

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CastVoteActivity.CAST_VOTE_REQUEST_CODE &&
            resultCode == CastVoteActivity.CAST_VOTE_RESULT_CODE) {
            accountParentRefresh.triggerRefresh(AccountPagerFragment.Page.VOTE)
        }
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<VoteIntent> = Observable.merge(
        Observable.just(VoteIntent.Init(fromBundle(arguments!!).eosAcconuntVote)),
        RxView.clicks(vote_cast_button).map { VoteIntent.NavigateToCastVote },
        RxView.clicks(vote_no_vote_castvote_button).map { VoteIntent.VoteForUs(eosAccount) }
    )

    override fun layout(): VoteViewLayout = this

    override fun model(): VoteViewModel = getViewModel(viewModelFactory)

    override fun render(): VoteViewRenderer = render

    override fun populateProxyVote(proxyVoter: String) {
        vote_proxy_group.visible()
        vote_proxy_voter.text = proxyVoter
    }

    override fun populateProducerVotes(eosAccountVote: EosAccountVote) {
        vote_producer_vote_group.visible()
        adapter.clear()
        adapter.populate(eosAccountVote.producers)
    }

    override fun showNoVoteCast() {
        vote_no_vote_group.visible()
    }

    override fun navigateToCastVote() {
        model().publish(VoteIntent.Idle)
        startActivityForResult(castVoteIntent(eosAccount, context!!), CastVoteActivity.CAST_VOTE_REQUEST_CODE)
    }

    override fun showVoteForUsProgress() {
        vote_no_vote_castvote_progressbar.visible()
        vote_no_vote_castvote_button.invisible()
    }

    override fun voteForUsSuccess() {
        accountParentRefresh.triggerRefresh(AccountPagerFragment.Page.VOTE)
    }

    override fun voteForUsError(message: String, log: String) {
        vote_no_vote_castvote_progressbar.gone()
        vote_no_vote_castvote_button.visible()

        AlertDialog.Builder(context!!)
            .setMessage(message)
            .setPositiveButton(R.string.transaction_view_log_position_button) { _, _ ->
                model().publish(VoteIntent.Idle)
                startActivity(TransactionLogActivity.transactionLogIntent(log, context!!))
            }
            .setNegativeButton(R.string.transaction_view_log_negative_button, null)
            .create()
            .show()
    }

    companion object {

        fun newInstance(eosAccount: EosAccount): VoteFragment = with (VoteFragment()) {
            arguments = toBundle(eosAccount)
            this
        }

        private fun toBundle(eosAccount: EosAccount): Bundle = with (Bundle()) {
            putParcelable("eosAccount", eosAccount)
            this
        }

        private fun fromBundle(bundle: Bundle): EosAccount = bundle.getParcelable("eosAccount")
    }
}
