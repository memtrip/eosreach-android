package com.memtrip.eosreach.app.account.vote.cast.producers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.resources.ResourcesFragment

import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import javax.inject.Inject

class CastProducersVoteFragment
    : MviFragment<CastProducersVoteIntent, CastProducersVoteRenderAction, CastProducersVoteViewState, CastProducersVoteViewLayout>(), CastProducersVoteViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: CastProducersVoteViewRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.cast_producers_vote_fragment, container, false)
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<CastProducersVoteIntent> = Observable.empty()

    override fun layout(): CastProducersVoteViewLayout = this

    override fun model(): CastProducersVoteViewModel = getViewModel(viewModelFactory)

    override fun render(): CastProducersVoteViewRenderer = render

    override fun showProgress() {

    }

    override fun showError() {

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
