package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.account.EosAccount
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.DelegateBandwidthFormFragment
import com.memtrip.eosreach.app.account.resources.manage.bandwidth.form.UnDelegateBandwidthFormFragment
import com.memtrip.eosreach.app.account.vote.cast.CastVotePagerFragment.Page.*

import com.memtrip.eosreach.app.account.vote.cast.producers.CastProducersVoteFragment
import com.memtrip.eosreach.app.account.vote.cast.proxy.CastProxyVoteFragment
import java.util.Arrays

class ManageBandwidthFragmentPagerAdapter(
    fragmentManager: FragmentManager,
    private val context: Context,
    private val eosAccount: EosAccount,
    private val pages: List<Page> = Arrays.asList(Page.DELEGATE, Page.UNDELEGATE),
    private val delegateBandwidthFormFragment: DelegateBandwidthFormFragment =
        DelegateBandwidthFormFragment.newInstance(eosAccount),
    private val unDelegateBandwidthFormFragment: UnDelegateBandwidthFormFragment =
        UnDelegateBandwidthFormFragment.newInstance(eosAccount)
) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getPageTitle(position: Int): CharSequence? {
        val page = pages[position]
        return when (page) {
            Page.DELEGATE -> context.getString(R.string.manage_bandwidth_tab_delegate)
            Page.UNDELEGATE -> context.getString(R.string.manage_bandwidth_tab_undelegate)
        }
    }

    override fun getItem(position: Int): Fragment {
        val page = pages[position]
        return when (page) {
            Page.DELEGATE -> delegateBandwidthFormFragment
            Page.UNDELEGATE -> unDelegateBandwidthFormFragment
        }
    }

    override fun getCount(): Int = pages.size

    enum class Page {
        DELEGATE,
        UNDELEGATE
    }
}