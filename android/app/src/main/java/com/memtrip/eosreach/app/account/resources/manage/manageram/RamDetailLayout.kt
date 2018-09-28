package com.memtrip.eosreach.app.account.resources.manage.manageram

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.Balance
import com.memtrip.eosreach.app.price.BalanceFormatter

import com.memtrip.eosreach.uikit.BalanceDetailsLayout
import com.memtrip.eosreach.uikit.BalanceDetailsLayoutImpl

import kotlinx.android.synthetic.main.ram_details_layout.view.*

class RamDetailLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), BalanceDetailsLayout by BalanceDetailsLayoutImpl() {

    init {
        LayoutInflater.from(context).inflate(R.layout.ram_details_layout, this)
    }

    fun populate(
        kb: String,
        pricePerKb: Balance
    ) {
        bandwidth_details_kb_value.text = kb
        bandwidth_details_cost_value.text = cost(kb, pricePerKb)
    }

    private fun cost(kb: String, pricePerKb: Balance): String {
        val kbValue = kb.toDouble()
        val costValue = kbValue * pricePerKb.amount
        return BalanceFormatter.formatEosBalance(costValue, pricePerKb.symbol)
    }
}