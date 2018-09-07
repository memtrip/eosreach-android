package com.memtrip.eosreach.app.account

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.account.balance.BalanceFragment
import com.memtrip.eosreach.app.account.resources.ResourcesFragment
import com.memtrip.eosreach.app.account.vote.CastVoteActivity
import com.memtrip.eosreach.app.account.vote.VoteFragment
import java.util.Arrays.asList

class AccountPagerFragment(
    fragmentManager: FragmentManager,
    context: Context,
    private val accountView: AccountView,
    private val pages: List<Page> = asList(
        Page.Balance(context.getString(R.string.account_page_balance)),
        Page.Resources(context.getString(R.string.account_page_resources)),
        Page.Vote(context.getString(R.string.account_page_vote))
    ),
    private val balanceFragment: BalanceFragment = BalanceFragment.newInstance(accountView.balances!!),
    private val resourcesFragment: ResourcesFragment = ResourcesFragment.newInstance(accountView.eosAccount!!),
    private val voteFragment: VoteFragment = VoteFragment.newInstance(accountView.eosAccount!!)
) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getPageTitle(position: Int): CharSequence? {
        val page = pages[position]
        return when (page) {
            is Page.Balance -> page.title
            is Page.Resources -> page.title
            is Page.Vote -> page.title
        }
    }

    override fun getItem(position: Int): Fragment {
        val page = pages[position]
        return when (page) {
            is Page.Balance -> balanceFragment
            is Page.Resources -> resourcesFragment
            is Page.Vote -> voteFragment
        }
    }

    override fun getCount(): Int = pages.size

    sealed class Page {
        data class Balance(val title: String) : Page()
        data class Resources(val title: String) : Page()
        data class Vote(val title: String) : Page()
    }
}