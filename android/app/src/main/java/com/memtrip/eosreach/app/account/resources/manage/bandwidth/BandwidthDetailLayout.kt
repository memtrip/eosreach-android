package com.memtrip.eosreach.app.account.resources.manage.bandwidth

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout

import com.memtrip.eosreach.R
import kotlinx.android.synthetic.main.bandwidth_details_layout.view.*

class BandwidthDetailLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.bandwidth_details_layout, this)
    }

    fun populate(cpu: String, net: String) {
        bandwidth_details_cpu_value.text = cpu
        bandwidth_details_net_value.text = net
    }
}