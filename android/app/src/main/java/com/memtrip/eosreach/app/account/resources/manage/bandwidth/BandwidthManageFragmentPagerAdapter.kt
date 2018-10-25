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
package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.DelegateBandwidthFormFragment
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.UnDelegateBandwidthFormFragment
import java.util.Arrays

class BandwidthManageFragmentPagerAdapter(
    fragmentManager: FragmentManager,
    private val context: Context,
    private val bandwidthFormBundle: BandwidthFormBundle,
    private val contractAccountBalance: ContractAccountBalance,
    private val pages: List<Page> = Arrays.asList(Page.DELEGATE, Page.UNDELEGATE, Page.ALLOCATED),
    private val delegateBandwidthFormFragment: DelegateBandwidthFormFragment =
        DelegateBandwidthFormFragment.newInstance(bandwidthFormBundle, contractAccountBalance),
    private val undelegateBandwidthFormFragment: UnDelegateBandwidthFormFragment =
        UnDelegateBandwidthFormFragment.newInstance(bandwidthFormBundle, contractAccountBalance),
    private val delegateBandwidthListFragment: DelegateBandwidthListFragment =
        DelegateBandwidthListFragment.newInstance(contractAccountBalance)
) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getPageTitle(position: Int): CharSequence? {
        val page = pages[position]
        return when (page) {
            Page.DELEGATE -> context.getString(R.string.resources_manage_bandwidth_tab_delegate)
            Page.UNDELEGATE -> context.getString(R.string.resources_manage_bandwidth_tab_undelegate)
            Page.ALLOCATED -> context.getString(R.string.resources_manage_bandwidth_tab_allocated)
        }
    }

    override fun getItem(position: Int): Fragment {
        val page = pages[position]
        return when (page) {
            Page.DELEGATE -> delegateBandwidthFormFragment
            Page.UNDELEGATE -> undelegateBandwidthFormFragment
            Page.ALLOCATED -> delegateBandwidthListFragment
        }
    }

    override fun getCount(): Int = pages.size

    enum class Page {
        DELEGATE,
        UNDELEGATE,
        ALLOCATED
    }
}