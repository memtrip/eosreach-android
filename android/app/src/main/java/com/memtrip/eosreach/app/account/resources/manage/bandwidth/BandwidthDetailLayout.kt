package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout

import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.price.BalanceFormatter
import com.memtrip.eosreach.uikit.BalanceDetailsLayout
import com.memtrip.eosreach.uikit.BalanceDetailsLayoutImpl
import kotlinx.android.synthetic.main.bandwidth_details_layout.view.*

class BandwidthDetailLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), BalanceDetailsLayout by BalanceDetailsLayoutImpl() {

    init {
        LayoutInflater.from(context).inflate(R.layout.bandwidth_details_layout, this)
    }

    fun populate(
        cpu: Balance,
        net: Balance,
        contractAccountBalance: ContractAccountBalance
    ) {
        bandwidth_details_cpu_value.text = formatBalance(cpu, contractAccountBalance, context)
        bandwidth_details_net_value.text = formatBalance(net, contractAccountBalance, context)
    }
}