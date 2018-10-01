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
package com.memtrip.eosreach.app.account

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.account.balance.BalanceFragment
import com.memtrip.eosreach.app.account.resources.ResourcesFragment
import com.memtrip.eosreach.app.account.vote.VoteFragment
import java.util.Arrays.asList

class AccountFragmentPagerAdapter(
    fragmentManager: FragmentManager,
    private val context: Context,
    private val accountView: AccountView,
    private val pages: List<Page> = asList(Page.BALANCE, Page.RESOURCES, Page.VOTE),
    private val balanceFragment: BalanceFragment = BalanceFragment.newInstance(
        accountView.eosAccount!!.accountName,
        accountView.balances!!),
    private val resourcesFragment: ResourcesFragment = ResourcesFragment.newInstance(
        accountView.eosAccount!!,
        contractAccountBalance(accountView)
    ),
    private val voteFragment: VoteFragment = VoteFragment.newInstance(accountView.eosAccount!!)
) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getPageTitle(position: Int): CharSequence? {
        val page = pages[position]
        return when (page) {
            Page.BALANCE -> context.getString(R.string.account_page_balance)
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

    companion object {
        fun contractAccountBalance(accountView: AccountView): ContractAccountBalance {
            return if (accountView.balances != null && accountView.balances.balances.isNotEmpty()) {
                accountView.balances.balances[0]
            } else {
                ContractAccountBalance.unavailable()
            }
        }
    }
}