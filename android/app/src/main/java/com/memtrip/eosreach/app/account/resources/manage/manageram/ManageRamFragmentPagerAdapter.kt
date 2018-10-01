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