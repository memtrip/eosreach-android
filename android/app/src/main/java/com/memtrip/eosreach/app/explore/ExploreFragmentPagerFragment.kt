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
package com.memtrip.eosreach.app.explore

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.memtrip.eosreach.R
import java.util.Arrays

class ExploreFragmentPagerFragment(
    fragmentManager: FragmentManager,
    private val context: Context,
    private val pages: List<Page> = Arrays.asList(Page.SEARCH, Page.BLOCK_PRODUCERS),
    private val searchFragment: SearchFragment = SearchFragment.newInstance(),
    private val registeredBlockProducersFragment: RegisteredBlockProducersFragment = RegisteredBlockProducersFragment.newInstance()
) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getPageTitle(position: Int): CharSequence? {
        val page = pages[position]
        return when (page) {
            Page.SEARCH -> context.getString(R.string.explore_tab_search)
            Page.BLOCK_PRODUCERS -> context.getString(R.string.explore_tab_block_producers)
        }
    }

    override fun getItem(position: Int): Fragment {
        val page = pages[position]
        return when (page) {
            Page.SEARCH -> searchFragment
            Page.BLOCK_PRODUCERS -> registeredBlockProducersFragment
        }
    }

    override fun getCount(): Int = pages.size

    enum class Page {
        SEARCH,
        BLOCK_PRODUCERS
    }
}