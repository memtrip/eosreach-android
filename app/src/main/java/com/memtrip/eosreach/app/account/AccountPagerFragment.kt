package com.memtrip.eosreach.app.account

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.memtrip.eosreach.R
import com.memtrip.eosreach.app.account.balance.BalanceFragment
import com.memtrip.eosreach.app.account.balance.BalanceIntent
import com.memtrip.eosreach.app.account.resources.ResourcesFragment
import java.util.Arrays.asList

class AccountPagerFragment(
    fragmentManager: FragmentManager,
    context: Context,
    private val accountView: AccountView,
    private val pages: List<Page> = asList(
        Page.Balance(context.getString(R.string.account_page_balance)),
        Page.Resources(context.getString(R.string.account_page_resources))
    ),
    private val balanceFragment: BalanceFragment = BalanceFragment.newInstance(accountView.balances!!),
    private val resourcesFragment: ResourcesFragment = ResourcesFragment.newInstance(accountView.eosAccount!!)
) : FragmentPagerAdapter(fragmentManager) {

    override fun getPageTitle(position: Int): CharSequence? {
        val page = pages[position]
        return when (page) {
            is Page.Balance -> page.title
            is Page.Resources -> page.title
        }
    }

    override fun getItem(position: Int): Fragment {
        val page = pages[position]
        return when (page) {
            is Page.Balance -> balanceFragment
            is Page.Resources -> resourcesFragment
        }
    }

    override fun getCount(): Int = pages.size

    sealed class Page {
        data class Balance(val title: String) : Page()
        data class Resources(val title: String) : Page()
    }
}