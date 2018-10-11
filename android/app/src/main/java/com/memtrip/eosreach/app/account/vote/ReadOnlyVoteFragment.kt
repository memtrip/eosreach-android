package com.memtrip.eosreach.app.account.vote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.memtrip.eosreach.app.account.AccountTheme

import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.account_vote_fragment.*
import kotlinx.android.synthetic.main.account_vote_fragment.view.*

class ReadOnlyVoteFragment : VoteFragment() {

    override fun accountTheme(): AccountTheme = AccountTheme.READ_ONLY

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)!!
        view.account_vote_navigation_group.gone()
        (view.vote_proxy_label.layoutParams as ConstraintLayout.LayoutParams).topMargin = 0
        (view.vote_no_vote_body_label.layoutParams as ConstraintLayout.LayoutParams).topMargin = 0
        (view.vote_producer_vote_title_label.layoutParams as ConstraintLayout.LayoutParams).topMargin = 0
        return view
    }

    override fun showNoVoteCast() {
        vote_ready_only_no_vote_group.visible()
    }

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }
}