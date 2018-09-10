package com.memtrip.eosreach.app.account

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.account.balance.BalanceFragment
import com.memtrip.eosreach.app.account.resources.ResourcesFragment
import com.memtrip.eosreach.app.account.vote.VoteFragment
import java.util.Arrays.asList

class AccountPagerFragment(
    fragmentManager: FragmentManager,
    private val context: Context,
    private val accountView: AccountView,
    private val pages: List<Page> = asList(Page.BALANCE, Page.RESOURCES, Page.VOTE),
    private val balanceFragment: BalanceFragment = BalanceFragment.newInstance(accountView.balances!!),
    private val resourcesFragment: ResourcesFragment = ResourcesFragment.newInstance(accountView.eosAccount!!),
    private val voteFragment: VoteFragment = VoteFragment.newInstance(accountView.eosAccount!!)
) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getPageTitle(position: Int): CharSequence? {
        val page = pages[position]
        return when (page) {
            Page.BALANCE-> context.getString(R.string.account_page_balance)
            Page.RESOURCES -> context.getString(R.string.account_page_resources)
            Page.VOTE -> context.getString(R.string.account_page_vote)
        }
    }

    override fun getItem(position: Int): Fragment {
        val page = pages[position]
        return when (page) {
            Page.BALANCE -> balanceFragment
            Page.RESOURCES -> resourcesFragment
            Page.VOTE -> voteFragment
        }
    }

    override fun getCount(): Int = pages.size

    enum class Page {
        BALANCE,
        RESOURCES,
        VOTE
    }
}