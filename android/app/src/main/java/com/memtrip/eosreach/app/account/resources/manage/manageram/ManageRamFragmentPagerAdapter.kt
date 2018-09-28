package com.memtrip.eosreach.app.account.resources.manage.manageram

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.account.resources.manage.manageram.form.BuyRamFormFragment
import com.memtrip.eosreach.app.account.resources.manage.manageram.form.SellRamFormFragment

import java.util.Arrays

class ManageRamFragmentPagerAdapter(
    fragmentManager: FragmentManager,
    private val context: Context,
    private val eosAccount: EosAccount,
    private val contractAccountBalance: ContractAccountBalance,
    private val ramPricePerKb: Balance,
    private val pages: List<Page> = Arrays.asList(Page.BUY, Page.SELL),
    private val buyRamFormFragment: BuyRamFormFragment = BuyRamFormFragment.newInstance(
        eosAccount,
        contractAccountBalance,
        ramPricePerKb
    ),
    private val sellRamFormFragment: SellRamFormFragment = SellRamFormFragment.newInstance(
        eosAccount,
        contractAccountBalance,
        ramPricePerKb
    )
) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getPageTitle(position: Int): CharSequence? {
        val page = pages[position]
        return when (page) {
            Page.BUY -> context.getString(R.string.resources_manage_ram_tab_buy)
            Page.SELL -> context.getString(R.string.resources_manage_ram_tab_sell)
        }
    }

    override fun getItem(position: Int): Fragment {
        val page = pages[position]
        return when (page) {
            Page.BUY -> buyRamFormFragment
            Page.SELL -> sellRamFormFragment
        }
    }

    override fun getCount(): Int = pages.size

    enum class Page {
        BUY,
        SELL
    }
}