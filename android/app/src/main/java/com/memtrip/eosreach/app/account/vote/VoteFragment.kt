package com.memtrip.eosreach.app.account.vote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.account.EosAccountVote
import com.memtrip.eosreach.api.actions.model.AccountAction
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.actions.AccountActionsAdapter
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.visible

import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_actions_activity.*
import kotlinx.android.synthetic.main.vote_fragment.*
import javax.inject.Inject

class VoteFragment
    : MviFragment<VoteIntent, VoteRenderAction, VoteViewState, VoteViewLayout>(), VoteViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: VoteViewRenderer

    private lateinit var adapter: VoteProducerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.vote_fragment, container, false)

        val adapterInteraction: PublishSubject<Interaction<String>> = PublishSubject.create()
        adapter = VoteProducerAdapter(context!!, adapterInteraction)
        vote_producer_vote_list_recyclerview.adapter = adapter

        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<VoteIntent> = Observable.empty()

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
