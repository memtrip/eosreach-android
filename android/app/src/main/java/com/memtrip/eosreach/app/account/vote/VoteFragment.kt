package com.memtrip.eosreach.app.account.vote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.memtrip.eosreach.app.MviActivity
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.resources.ResourcesFragment

import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import javax.inject.Inject

import kotlinx.android.synthetic.main.vote_activity.*

class VoteFragment
    : MviFragment<VoteIntent, VoteRenderAction, VoteViewState, VoteViewLayout>(), VoteViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: VoteViewRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.vote_activity, container, false)
        return view
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun intents(): Observable<VoteIntent> = Observable.empty()

    override fun layout(): VoteViewLayout = this

    override fun model(): VoteViewModel = getViewModel(viewModelFactory)

    override fun render(): VoteViewRenderer = render

    override fun showProgress() {

    }

    override fun showError() {

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
