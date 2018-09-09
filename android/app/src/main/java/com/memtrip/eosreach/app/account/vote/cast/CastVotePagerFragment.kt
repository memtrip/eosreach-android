package com.memtrip.eosreach.app.account.vote.cast

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.account.AccountView

import com.memtrip.eosreach.app.account.vote.cast.producers.CastProducersVoteFragment
import com.memtrip.eosreach.app.account.vote.cast.proxy.CastProxyVoteFragment
import java.util.Arrays

class CastVotePagerFragment(
    fragmentManager: FragmentManager,
    context: Context,
    private val accountView: AccountView,
    private val pages: List<Page> = Arrays.asList(
        Page.Producer(context.getString(R.string.cast_vote_page_block_producers)),
        Page.Proxy(context.getString(R.string.cast_vote_page_proxy))
    ),
    private val producerFragment: CastProducersVoteFragment = CastProducersVoteFragment.newInstance(accountView.balances!!),
    private val proxyFragment: CastProxyVoteFragment = CastProxyVoteFragment.newInstance(accountView.eosAccount!!),
    ) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getPageTitle(position: Int): CharSequence? {
        val page = pages[position]
        return when (page) {
            is Page.Producer -> page.title
            is Page.Proxy -> page.title
        }
    }

    override fun getItem(position: Int): Fragment {
        val page = pages[position]
        return when (page) {
            is Page.Producer -> producerFragment
            is Page.Proxy -> proxyFragment
        }
    }

    override fun getCount(): Int = pages.size

    sealed class Page {
        data class Producer(val title: String) : Page()
        data class Proxy(val title: String) : Page()
    }
}