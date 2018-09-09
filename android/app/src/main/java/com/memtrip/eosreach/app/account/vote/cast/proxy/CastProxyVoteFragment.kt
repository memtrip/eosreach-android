package com.memtrip.eosreach.app.account.vote.cast.proxy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.vote.cast.producers.CastProducersVoteFragment

import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import javax.inject.Inject

class CastProxyVoteFragment
    : MviFragment<CastProxyVoteIntent, CastProxyVoteRenderAction, CastProxyVoteViewState, CastProxyVoteViewLayout>(), CastProxyVoteViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: CastProxyVoteViewRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.cast_proxy_vote_fragment, container, false)
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<CastProxyVoteIntent> = Observable.empty()

    override fun layout(): CastProxyVoteViewLayout = this

    override fun model(): CastProxyVoteViewModel = getViewModel(viewModelFactory)

    override fun render(): CastProxyVoteViewRenderer = render

    override fun showProgress() {
    }

    override fun showError() {
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
